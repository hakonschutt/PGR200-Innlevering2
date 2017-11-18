package com.innlevering.initDatabase;

import com.innlevering.initDatabase.exception.InitDBFileNotFoundException;
import com.innlevering.initDatabase.exception.InitDBIOException;
import com.innlevering.initDatabase.exception.InitDBSQLException;

import java.io.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Class is used to controll database overwriting and creating.
 * Created by hakonschutt on 22/10/2017.
 */
public class DBValidationHandler {
    private Boolean isScanned = true;

    /**
     * Retrieves database name from property file
     * @return
     * @throws InitDBIOException
     */
    private String getDatabaseName() throws InitDBIOException {
        try {
            Properties properties = new Properties();
            InputStream input = new FileInputStream("data.properties");
            properties.load(input);

            return properties.getProperty("db");
        } catch (IOException e){
            throw new InitDBIOException("Unable to read file. Make sure its formatted correctly.");
        }
    }

    /**
     * Method is used to return all tables in a String array format
     * @param con
     * @return
     * @throws InitDBIOException
     * @throws InitDBSQLException
     */
    public String[] getAllTables(Connection con) throws InitDBIOException, InitDBSQLException {
        String database = getDatabaseName();
        String sql = "SHOW TABLES FROM " + database;

        List<String> tables = new ArrayList<>();

        try (Statement stmt = con.createStatement()) {
            ResultSet res = stmt.executeQuery(sql);

            if(!res.next()) return new String[0];

            do {
                tables.add(res.getString(1));
            } while (res.next());
        } catch (SQLException e){
            throw new InitDBSQLException(InitDBSQLException.getErrorMessage("queryTables"));
        }

        return tables.toArray(new String[tables.size()]);
    }

    /**
     * Deletes the database if the user wants to overwrite the current database
     * @param con
     * @param dbName
     * @throws InitDBSQLException
     */
    public void overWriteDatabase( Connection con, String dbName ) throws InitDBSQLException {
        try ( Statement stmt = con.createStatement() ){
            stmt.executeUpdate("DROP DATABASE IF EXISTS " + dbName +  "");
            createDataBase( con, dbName );
        } catch (SQLException e){
            throw new InitDBSQLException(InitDBSQLException.getErrorMessage("overwriteDatabase"));
        }
    }

    /**
     * Creates a new database if the user has entered a new database name or want to overwrite the current database
     * @param con
     * @param newDbName
     * @throws InitDBSQLException
     */
    public void createDataBase( Connection con, String newDbName ) throws InitDBSQLException {
        try (Statement stmt = con.createStatement()){
            stmt.executeUpdate("CREATE DATABASE " + newDbName +  "");
            this.isScanned = false;
        } catch (SQLException e){
            throw new InitDBSQLException(InitDBSQLException.getErrorMessage("createDatabase"));
        }
    }

    /**
     * Validates if the database already exists.
     * @param con
     * @param databaseName
     * @return
     * @throws InitDBSQLException
     */
    public boolean validateIfDBExists( Connection con, String databaseName ) throws InitDBSQLException {
        try (Statement stmt = con.createStatement();
             ResultSet res =
                     stmt.executeQuery(
                             "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '"
                                     + databaseName + "'")){

            return res.next();
        } catch (SQLException e){
            throw new InitDBSQLException(InitDBSQLException.getErrorMessage("noValidation"));
        }
    }

    /**
     * Fixes foreign keys on table so everything works after thread has run the upload.
     * @param fileName
     * @throws InitDBFileNotFoundException
     * @throws InitDBIOException
     * @throws InitDBSQLException
     */
    public void fixForeignKeysForTable(String fileName) throws InitDBFileNotFoundException, InitDBIOException, InitDBSQLException {
        String file = "input/" + fileName;

        try (BufferedReader in = new BufferedReader(new FileReader(file))){
            String table = in.readLine();

            for (int i = 0; i < 4; i++) in.readLine();

            String[] FK = in.readLine().split("/");

            if(!FK[0].equals("nofk")){
                String sql;

                sql = "ALTER TABLE `" + table + "` ";
                for(int i = 0; i < FK.length; i+=3){
                    sql += "ADD FOREIGN KEY (" + FK[i] + ") REFERENCES " + FK[i + 1] + "(" + FK[i + 2] + ")";
                    if(i == FK.length - 3){
                        sql += "";
                    } else {
                        sql += ", ";
                    }
                }

                executeUpdate(sql);
            }
        } catch (FileNotFoundException e){
            throw new InitDBFileNotFoundException("Unable locate " + fileName + ".");
        } catch (IOException e){
            throw new InitDBIOException("Unable to read file. Make sure its formatted correctly.");
        }
    }

    /**
     * Executes the alter that fixes the foreign key issue.
     * @param sql
     * @throws InitDBFileNotFoundException
     * @throws InitDBIOException
     * @throws InitDBSQLException
     */
    public void executeUpdate(String sql) throws InitDBFileNotFoundException, InitDBIOException, InitDBSQLException {
        SetupConnection db = new SetupConnection();
        try (Connection con = db.getConnection();
             Statement stmt = con.createStatement()){
            stmt.executeUpdate(sql);
        } catch (SQLException e){
            throw new InitDBSQLException(InitDBSQLException.getErrorMessage("executeUpdateFK"));
        }
    }

    public Boolean getIsScanned() {
        return isScanned;
    }
}
