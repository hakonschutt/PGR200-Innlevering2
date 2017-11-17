package com.innlevering.server.database;

import com.innlevering.exception.ServerFileNotFoundException;
import com.innlevering.exception.ServerIOException;
import com.innlevering.exception.ServerSQLException;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Main database class. It creates a connection to the database.
 * Created by hakonschutt on 22/10/2017.
 */
public class DBConnection {

    /**
     * Returns a connection to the database.
     * @return
     */
    public Connection getConnection() throws ServerFileNotFoundException, ServerIOException, ServerSQLException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("data.properties")) {
            MysqlDataSource ds = new MysqlDataSource();
            properties.load(input);

            ds.setUser(properties.getProperty("user"));
            ds.setPassword(properties.getProperty("pass"));
            ds.setDatabaseName(properties.getProperty("db"));
            ds.setServerName(properties.getProperty("host"));

            Connection connect = ds.getConnection();

            return connect;
        } catch (FileNotFoundException e){
            throw new ServerFileNotFoundException();
        } catch (IOException e){
            throw new ServerIOException(ServerIOException.getErrorMessage("readProperties"));
        } catch (SQLException e){
            throw new ServerSQLException(ServerSQLException.getErrorMessage("connection"));
        }
    }

    /**
     * Returns the databasename from the property file.
     * @return
     */
    public String getDatabaseName() throws ServerFileNotFoundException, ServerIOException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("data.properties")) {
            properties.load(input);
            return properties.getProperty("db");
        } catch (FileNotFoundException e){
            throw new ServerFileNotFoundException();
        } catch (IOException e){
            throw new ServerIOException(ServerIOException.getErrorMessage("readProperties"));
        }
    }
}
