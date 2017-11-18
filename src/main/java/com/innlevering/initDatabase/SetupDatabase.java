package com.innlevering.initDatabase;

import com.innlevering.initDatabase.exception.InitDBFileNotFoundException;
import com.innlevering.initDatabase.exception.InitDBIOException;
import com.innlevering.initDatabase.exception.InitDBSQLException;

import java.io.*;
import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by hakonschutt on 22/10/2017.
 * Init database connection. This is to create a database with all the necassary tables.
 * This insures that the server has a stable connection with all neccassaties. This is also validated
 * when the server starts. Running this first vil insure that the server can start properly
 */
public class SetupDatabase {
    private SetupConnection connect = new SetupConnection();
    private DBValidationHandler handler = new DBValidationHandler();
    private SetupConnection tempConnect;
    private Scanner sc = new Scanner( System.in );

    public SetupDatabase() {}

    public static void main(String[] args) {
        new SetupDatabase().startInitOfDatabase();
    }

    /**
     * Main setup method. Runs through the necessary methods to execute the setupDatabase.
     */
    public void startInitOfDatabase() {
        boolean setUpNewDB = !usePropertyEntries();

        while (setUpNewDB){
            setUpDatabase();

            if(!handler.getIsScanned()){
                System.out.println("Starting importing files to database");

                try {
                    FileUploadHandler rf = new FileUploadHandler();
                    rf.startInputScan();
                } catch (InitDBFileNotFoundException | InitDBIOException | InitDBSQLException e){
                    System.err.println(e.getMessage());
                }

                setUpNewDB = false;
            } else {
                if(canUsePropertyEntry()){
                    setUpNewDB = false;
                } else {
                    System.out.println("Unable to use the entry.");
                }
            }
        }

        System.out.println("All tables are currently present.");
        System.out.println("The server is now safe to run.");
    }

    /**
     * Runs a user request if the properties are usable. IF not it returns false.
     * @return
     */
    public boolean usePropertyEntries() {
        String useDatabaseEntry = "no";

        if(canUsePropertyEntry()){
            System.out.println("Able to use current connection!");

            try {
                printPropertyEntry();
            } catch (InitDBIOException e){
                System.err.println(e.getMessage());
            }

            System.out.print("Use this connection?( yes / no ) ");
            useDatabaseEntry = sc.nextLine();
        }

        return useDatabaseEntry.equals("yes");
    }


    /**
     * Checks if the server can run on the current property entries.
     * @return
     */
    public boolean canUsePropertyEntry() {
        try {
            Connection con = connect.getConnection();
            String[] tables = handler.getAllTables( con );

            return validateTables( tables );

        } catch (InitDBFileNotFoundException | InitDBIOException | InitDBSQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    /**
     * Validates if the tables are present in the database.
     * @param tables
     * @return
     */
    private boolean validateTables(String[] tables) {
        int count = 0;

        if (tables.length > 0){
            for(int i = 0; i < tables.length; i++){
                if(tables[i].equals("field_of_study") || tables[i].equals("room") || tables[i].equals("subject") || tables[i].equals("teacher")){
                    count++;
                }
            }
        }

        return count == 4;
    }

    /**
     * Prints property entries.
     * @throws IOException
     */
    public void printPropertyEntry() throws InitDBIOException {
        try {
            Properties properties = new Properties();
            InputStream input = new FileInputStream("data.properties");
            properties.load(input);

            System.out.println("Database: " + properties.getProperty("db"));
            System.out.println("Host: " + properties.getProperty("host"));
            System.out.println("Username: " + properties.getProperty("user"));
            System.out.println("Password: " + properties.getProperty("pass"));
        } catch (IOException e){
            throw new InitDBIOException("Unable to read from property file.");
        }

    }

    /**
     * The method tries to setup a database connection. It runs until the entries are valid.
     * When the values are evaluated it writes to the property file.
     */
    public void setUpDatabase() {
        System.out.println("Setting up a new connection!");
        boolean works = false;
        while ( !works ){
            works = writeDbInfo();
            System.out.println();
        }

        try{
            writeProperties();
        } catch (InitDBIOException e){
            System.err.println(e.getMessage() + "\n");
        }
    }

    /**
     * Method askes for user input for database connection
     * @return
     */
    private boolean writeDbInfo() {
        String[] dbInfo = new String[4];
        System.out.print( "DB user: " );
        dbInfo[0] = sc.nextLine();
        System.out.print( "DB pass: ") ;
        dbInfo[1] = sc.nextLine();
        System.out.print( "DB host: " );
        dbInfo[2] = sc.nextLine();
        System.out.print( "DB name: " );
        dbInfo[3] = sc.nextLine();

        return connectToDatabase( dbInfo );
    }

    /**
     * Writing user input to property file AFTER checking if the connection works with database
     * @throws InitDBIOException
     */
    private void writeProperties() throws InitDBIOException {
        Properties properties = new Properties();
        try (OutputStream outputStream = new FileOutputStream("data.properties")) {
            properties.setProperty("user", tempConnect.getUser());
            properties.setProperty("pass", tempConnect.getPass());
            properties.setProperty("host", tempConnect.getHost());
            properties.setProperty("db", tempConnect.getDbName());

            properties.store(outputStream, null);
        } catch (IOException e){
            throw new InitDBIOException("Unable to write to property file.");
        }
    }

    /**
     * Method tests the connection with the user input
     * @param dbInfo
     * @return
     */
    private boolean connectToDatabase( String[] dbInfo ) {
        tempConnect = new SetupConnection(dbInfo[0], dbInfo[1], dbInfo[2], dbInfo[3]);

        try {
            Connection con = tempConnect.verifyConnectionWithUserInput(false );
            boolean dbExists = handler.validateIfDBExists(con, dbInfo[3]);

            if(!dbExists){
                try {
                    handler.createDataBase(con, dbInfo[3]);
                    System.out.println("Creating database: " + dbInfo[3]);
                } catch (InitDBSQLException e) {
                    System.err.println(e.getMessage() + "\n");
                }
            } else {
                userInputForConnectionTest(con, dbInfo[3]);
            }
            return true;

        } catch (InitDBSQLException e){
            System.err.println(e.getMessage() + "\n");
        }

        System.out.print("Try again: ");
        return false;
    }

    /**
     * Prints user instructions for when a database already exists.
     */
    private void userInputForConnectionInstruction(){
        System.out.println("The database already exists!");
        System.out.println("How would you like to continue?");
        System.out.println("(1) Continue with this connection");
        System.out.println("(2) Change to a new database name");
        System.out.println("(3) !Overwrite the current database!");

    }

    /**
     * User input IF the database already exists
     */
    private void userInputForConnectionTest( Connection con, String dbName ) {
        userInputForConnectionInstruction();
        int asw;

        while(true){
            try {
                asw = Integer.parseInt(sc.nextLine());
            } catch (InputMismatchException | NumberFormatException e){
                System.err.println("The entry is not valid. Try again: ");
                continue;
            }

            if (asw <= 3 && asw > 0){
                break;
            } else {
                System.err.println("The entry is out of range. Try again: ");
            }
        }

        switch(asw){
            case 1:
                System.out.print( "Connection to " + dbName );
                break;
            case 2:
                changeDatabaseName(con);
                break;
            case 3:
                try {
                    handler.overWriteDatabase( con, dbName );
                    System.out.print( "Overwriting database " + dbName );
                } catch (InitDBSQLException e){
                    System.err.println(e.getMessage() + "\n");
                }
                break;
            default:
                System.out.println( "Not a valid command." );
                System.out.print( "Connection to " + dbName );
                break;
        }
    }

    /**
     * Lets the user change the database name IF the database already exists
     */
    private void changeDatabaseName( Connection con ) {
        System.out.print( "Whats the new name:" );
        Scanner sc = new Scanner( System.in );
        String newDbName = sc.nextLine();
        System.out.println();

        boolean dbExists = false;

        try {
            dbExists = handler.validateIfDBExists( con, newDbName );
        } catch (InitDBSQLException e){
            System.err.println("\n" + e.getMessage() + "\n");
        }


        if ( !dbExists ) {
            try {
                handler.createDataBase( con, newDbName );
                System.out.print( "Creating database: " + newDbName );
            } catch (InitDBSQLException e){
                System.err.println("\n" + e.getMessage() + "\n");
            }
            connect.setDbName( newDbName );
        } else {
            userInputForConnectionTest( con, newDbName );
        }
    }
}
