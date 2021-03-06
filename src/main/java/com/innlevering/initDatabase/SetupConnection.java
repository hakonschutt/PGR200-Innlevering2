package com.innlevering.initDatabase;

import com.innlevering.initDatabase.exception.InitDBFileNotFoundException;
import com.innlevering.initDatabase.exception.InitDBIOException;
import com.innlevering.initDatabase.exception.InitDBSQLException;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Main Connection class. All initDatabase classes implements this classes getConnection.
 * Created by hakonschutt on 22/10/2017.
 */
public class SetupConnection {
    private String user;
    private String pass;
    private String host;
    private String dbName;

    /**
     * Empty constructor if the user is not testing, and doesnt want to set
     * the table data but use the variables from property file
     */
    public SetupConnection(){}

    /**
     * Second constructor that lets the user set custom user, pass, host and dbname.
     * Primarily used for verifying user connection before writing properties.
     * @param user
     * @param pass
     * @param host
     * @param dbName
     */
    public SetupConnection(String user, String pass, String host, String dbName) {
        this.user = user;
        this.pass = pass;
        this.host = host;
        this.dbName = dbName;
    }

    /**
     * Method can be called to test connection with or without database information
     * This method is used when setting up the database information.
     * It tests if it can access the database with the user information.
     * @param withDatabaseConnection
     * @return
     * @throws InitDBSQLException
     */
    public Connection verifyConnectionWithUserInput(boolean withDatabaseConnection) throws InitDBSQLException {
        try {
            MysqlDataSource ds = new MysqlDataSource();

            if(withDatabaseConnection)
                ds.setDatabaseName(this.dbName);

            ds.setServerName(this.host);
            ds.setUser(this.user);
            ds.setPassword(this.pass);

            Connection connect = ds.getConnection();

            return connect;
        } catch (SQLException e){
            throw new InitDBSQLException(InitDBSQLException.getErrorMessage("connection"));
        }
    }

    /**
     * DBConnection getConnection. Used throughout the program to get the database connection
     * @return
     * @throws InitDBFileNotFoundException
     * @throws InitDBIOException
     * @throws InitDBSQLException
     */
    public Connection getConnection() throws InitDBFileNotFoundException, InitDBIOException, InitDBSQLException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("data.properties")) {
            MysqlDataSource ds = new MysqlDataSource();
            properties.load(input);

            ds.setDatabaseName(properties.getProperty("db"));
            ds.setServerName(properties.getProperty("host"));
            ds.setUser(properties.getProperty("user"));
            ds.setPassword(properties.getProperty("pass"));

            Connection connect = ds.getConnection();

            return connect;
        } catch (FileNotFoundException e){
            throw new InitDBFileNotFoundException("Unable to locate property file. Make sure its not deleted.");
        } catch (IOException e){
            throw new InitDBIOException("Unable to read file. Make sure its formatted correctly.");
        } catch (SQLException e){
            throw new InitDBSQLException(InitDBSQLException.getErrorMessage("connection"));
        }
    }

    public String getUser() { return user; }
    public String getPass() { return pass; }
    public String getHost() { return host; }
    public String getDbName() { return dbName; }
    public void setDbName(String dbName) { this.dbName = dbName; }

}
