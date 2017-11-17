package com.innlevering.server.database;

import com.innlevering.exception.ServerFileNotFoundException;
import com.innlevering.exception.ServerIOException;
import com.innlevering.exception.ServerSQLException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * This class validates if all the necassary classes are present for the server to run.
 * Created by hakonschutt on 22/10/2017.
 */
public class DBValidator {
    private DBConnection connect = new DBConnection();

    /**
     * Tests if connection works.
     * gets all the tables and returns true if the validateTables is true. else it returns false.
     * @return
     */
    public boolean startDatabaseCheck () throws ServerFileNotFoundException, ServerIOException, ServerSQLException {
        String[] tables = getAllTables();
        return tables.length > 0 && validateTables( tables );
    }

    /**
     * Gets a list of tables, and checks if they are the same as what the program needs.
     * @param tables
     * @return
     */
    private boolean validateTables(String[] tables) {
        if (tables.length < 1) return false;

        int count = 0;
        for(int i = 0; i < tables.length; i++){
            if(tables[i].equals("field_of_study") || tables[i].equals("room") || tables[i].equals("subject") || tables[i].equals("teacher")){
                count++;
            }
        }
        return count == 4;
    }

    /**
     * Returns the database name in the property file.
     * @return
     * @throws IOException
     */
    private String getDatabaseName() throws ServerFileNotFoundException, ServerIOException {
        try {
            Properties properties = new Properties();
            InputStream input = new FileInputStream("data.properties");
            properties.load(input);

            return properties.getProperty("db");
        }  catch (FileNotFoundException e){
            throw new ServerFileNotFoundException();
        } catch (IOException e){
            throw new ServerIOException(ServerIOException.getErrorMessage("readProperties"));
        }
    }

    /**
     * Returns an array with all the tables in the database
     * @return
     * @throws Exception
     */
    public String[] getAllTables() throws ServerFileNotFoundException, ServerIOException, ServerSQLException {
        String sql = "SHOW TABLES FROM " + getDatabaseName();
        String[] tables = new String[9];

        try (Connection con = connect.getConnection();
                Statement stmt = con.createStatement()) {
            int i = 0;
            ResultSet res = stmt.executeQuery(sql);
            if(!res.next()) {
                throw new SQLException("No tables where found");
            }
            do {
                tables[i] = res.getString(1);
                i++;
            } while (res.next());
        } catch (SQLException e){
            throw new ServerSQLException(ServerSQLException.getErrorMessage("tables"));
        }

        return tables;
    }
}
