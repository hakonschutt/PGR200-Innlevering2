package innlevering.initDatabase;

import java.io.*;
import java.sql.Connection;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by hakonschutt on 22/10/2017.
 * Init database connection. This is to create a database with all the necassary tables.
 * This insures that the server has a stable connection with all neccassaties. This is also validated
 * when the server startes. Running this first vil insure that the server can start properly
 *
 */
public class SetupDatabase {
    private SetupConnection connect = new SetupConnection();
    private DBValidationHandler handler = new DBValidationHandler();
    private SetupConnection tempConnect;
    private Scanner sc = new Scanner( System.in );

    public SetupDatabase() throws IOException {}

    public static void main(String[] args) throws Exception {
        new SetupDatabase().startInitOfDatabase();
    }

    public void startInitOfDatabase() throws Exception {
        boolean setUpNewDB = !usePropertyEntries();

        while (setUpNewDB){
            setUpDatabase();

            if(!handler.getScanned()){
                System.out.println("Starting importing files to database");
                FileUploadHandler rf = new FileUploadHandler();
                rf.startInputScan();
                setUpNewDB = false;
            } else {
                if(canUsePropertyEntry()){
                    setUpNewDB = false;
                }
            }
        }

        TimeUnit.SECONDS.sleep(2);
        System.out.println("All tables are currently present.");
        System.out.println("The server is now safe to run.");
    }

    public boolean usePropertyEntries() throws IOException {
        if(canUsePropertyEntry()){
            System.out.println("Able to use current connection!");
            printPropertyEntry();
            System.out.print("Use this connection?( yes / no ) ");
            String useDatabaseEntry = sc.nextLine();

            if(useDatabaseEntry.equals("yes")) return true;
        }

        return false;
    }


    public boolean canUsePropertyEntry(){
        try (Connection con = connect.getConnection()){
            String[] tables = handler.getAlleTables( con );
            return validateTables( tables );
        } catch (Exception e){
            System.out.println("Unable to connection with property entry...");
            return false;
        }
    }

    private boolean validateTables(String[] tables) {
        int count = 0;
        for(int i = 0; i < tables.length; i++){
            if(tables[i].equals("field_of_study") || tables[i].equals("room") || tables[i].equals("subject") || tables[i].equals("teacher")){
                count++;
            }
        }

        return count == 4;
    }

    public void printPropertyEntry() throws IOException {
        Properties properties = new Properties();
        InputStream input = new FileInputStream("data.properties");
        properties.load(input);

        System.out.println("Database: " + properties.getProperty("db"));
        System.out.println("Host: " + properties.getProperty("host"));
        System.out.println("Username: " + properties.getProperty("user"));
        System.out.println("Password: " + properties.getProperty("pass"));
    }

    public void setUpDatabase() throws Exception {
        System.out.println("Setting up a new connection!");
        boolean works = false;
        while ( !works ){
            works = writeDBinfo();
            System.out.println();
        }

        writeProperties();
    }

    private boolean writeDBinfo() throws Exception {
        String[] dbInfo = new String[4];
        System.out.println();
        System.out.print( "DB user: " );
        dbInfo[0] = sc.nextLine();
        System.out.print( "DB pass: ") ;
        dbInfo[1] = sc.nextLine();
        System.out.print( "DB host: " );
        dbInfo[2] = sc.nextLine();
        System.out.print( "DB name: " );
        dbInfo[3] = sc.nextLine();

        return connectToDatabase(dbInfo);
    }

    private void writeProperties() throws IOException {
        Properties properties = new Properties();
        OutputStream outputStream = new FileOutputStream("data.properties" );;

        properties.setProperty("user", tempConnect.getUser());
        properties.setProperty("pass", tempConnect.getPass());
        properties.setProperty("host", tempConnect.getHost());
        properties.setProperty("db", tempConnect.getDbName());

        properties.store(outputStream, null);
    }

    private boolean connectToDatabase ( String[] dbInfo ) throws Exception {
        tempConnect = new SetupConnection(dbInfo[0], dbInfo[1], dbInfo[2], dbInfo[3]);

        try (Connection con = tempConnect.verifyConnectionWithUserInput(false )){
            boolean dbExists = handler.validateIfDBExists(con, dbInfo[3]);

            if(!dbExists){
                handler.createDataBase(con, dbInfo[3]);
                System.out.print("Creating database: " + dbInfo[3]);
            } else {
                userInputForConnectionTest(con, dbInfo[3]);
            }

            return true;

        } catch (Exception e){
            System.out.println("Unable to connect with the current information");
            System.out.println("Try again: ");
            System.out.println();

            return false;
        }
    }

    private void userInputForConnectionInstruction(){
        System.out.println("The database already exists!");
        System.out.println("How would you like to continue?");
        System.out.println("(1) Continue with this connection");
        System.out.println("(2) Change to a new database name");
        System.out.println("(3) !Overwrite the current database!");
    }

    private void userInputForConnectionTest( Connection con, String dbName ) throws Exception {
        userInputForConnectionInstruction();
        int asw = sc.nextInt();
        switch(asw){
            case 1:
                System.out.print( "Connection to " + dbName );
                break;
            case 2:
                changeDatabaseName(con);
                break;
            case 3:
                System.out.print( "Overwriting database " + dbName );
                handler.overWriteDatabase( con, dbName );
                break;
            default:
                System.out.println( "Not a valid command." );
                System.out.print( "Connection to " + dbName );
                break;
        }
    }

    private void changeDatabaseName( Connection con ) throws Exception {
        System.out.print( "Whats the new name:" );
        Scanner sc = new Scanner( System.in );
        String newDbName = sc.nextLine();
        System.out.println();

        boolean dbExists = handler.validateIfDBExists( con, newDbName );

        if ( !dbExists ) {
            handler.createDataBase( con, newDbName );
            System.out.print( "Creating database: " + newDbName );
            connect.setDbName( newDbName );
        } else {
            userInputForConnectionTest( con, newDbName );
        }
    }
}
