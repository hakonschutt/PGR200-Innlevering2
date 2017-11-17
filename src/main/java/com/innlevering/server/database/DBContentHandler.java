package com.innlevering.server.database;

import com.innlevering.server.handlers.FormatHandler;

import java.io.IOException;
import java.sql.*;

/**
 * Main database handler for this program.
 * Created by hakonschutt on 23/10/2017.
 */
public class DBContentHandler {
    private DBHandler handler;
    private DBConnection connect;

    /**
     * Sets up all necassary fields.
     */
    public DBContentHandler() {
        handler = new DBHandler();
        connect = new DBConnection();
    }

    /**
     * Returns all tables in the database.
     * @return
     * @throws Exception
     */
    public String[] getAllTables() throws IOException, SQLException {
        String sql = handler.prepareQuery();
        String[] tables = new String[handler.getCount(handler.getTableCountQuery())];

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
            throw new SQLException("Unable to connect with current connection");
        }

        return tables;
    }

    /**
     * returns all the columns in a given table
     * @param tableName
     * @return
     * @throws Exception
     */
    public String[] getAllColumns(String tableName) throws IOException, SQLException {
        int size = handler.getCount( handler.getColumnCountQuery( tableName ) );
        String query = handler.prepareColumnDataQuery( tableName );
        String[] columns = getColumnNames(query, size);

        return columns;
    }

    /**
     * Returns all content based on the search
     * @param searchString
     * @param columns
     * @param chosenColumn
     * @param tableName
     * @return
     * @throws Exception
     */
    public String[] getSearchContent(String searchString, String[] columns, String chosenColumn, String tableName) throws IOException, SQLException {
        String format = FormatHandler.getFormatFromHandler(tableName);
        int entrySize = handler.getSearchCount(handler.getTableEntriesCountFromSearch( tableName, chosenColumn ), searchString);
        entrySize = entrySize > 0 ? entrySize : 0;
        String[] data = new String[entrySize + 1];

        if(entrySize > 0){
            String tempQuery = prepareTableQuery(tableName, columns);
            String finalQuery = prepareSearchQuery(tempQuery, chosenColumn);

            String[] tempData = getAllTableContentFromSearch(finalQuery, columns, entrySize, format, searchString);

            data[0] = formatSingleString(columns, format);

            for (int i = 1; i < data.length; i++){
                data[i] = tempData[i-1];
            }
        } else {
            data[0] = String.format("%-20s", "No data was found with this search!");
        }

        return data;
    }

    /**
     * Returns all table content for a given table
     * @param tableName
     * @return
     * @throws Exception
     */
    public String[] getTableContent(String tableName) throws IOException, SQLException {
        String format = FormatHandler.getFormatFromHandler(tableName);
        int entrySize = handler.getCount(handler.getTableEntriesCount( tableName ));

        String[] data = new String[entrySize + 1];
        int size = handler.getCount( handler.getColumnCountQuery( tableName ) );
        String query = handler.prepareColumnDataQuery( tableName );

        String[] columns = getColumnNames(query, size);
        String finalQuery = prepareTableQuery(tableName, columns);
        String[] tempData = getAllTableContent(finalQuery, columns, entrySize, format);

        data[0] = formatSingleString(columns, format);
        for (int i = 1; i < data.length; i++){
            data[i] = tempData[i-1];
        }

        return data;
    }

    /**
     * Return all table content based on a search
     * @param finalQuery
     * @param columns
     * @param entrySize
     * @param format
     * @param searchString
     * @return
     */
    private String[] getAllTableContentFromSearch(String finalQuery, String[] columns, int entrySize, String format, String searchString) throws IOException, SQLException {
        String[] dataEntries = new String[entrySize];

        try (Connection con = connect.getConnection();
             PreparedStatement ps = con.prepareStatement(finalQuery)) {
            ps.setString(1, searchString);
            ResultSet res = ps.executeQuery();
            int i = 0;

            while(res.next()){
                String[] tempData = new String[columns.length];
                for(int j = 0; j < columns.length; j++){
                    tempData[j] = String.valueOf(res.getObject(columns[j]));
                }
                dataEntries[i] = formatSingleString(tempData, format);
                i++;
            }
        }

        return dataEntries;
    }

    /**
     * Returns all table content based on query
     * @param sql
     * @param columns
     * @param entrySize
     * @param format
     * @return
     */
    private String[] getAllTableContent(String sql, String[] columns, int entrySize, String format) throws IOException, SQLException {
        String[] dataEntries = new String[entrySize];

        try (Connection con = connect.getConnection();
             Statement stmt = con.createStatement()) {
            ResultSet res = stmt.executeQuery(sql);
            if(!res.next()) {
                throw new SQLException("No tables where found");
            }
            int i = 0;

            do {
                String[] tempData = new String[columns.length];
                for(int j = 0; j < columns.length; j++){
                    tempData[j] = String.valueOf(res.getObject(columns[j]));
                }
                dataEntries[i] = formatSingleString(tempData, format);
                i++;

            } while (res.next());
        }

        return dataEntries;
    }

    /**
     * Returns all column names from a given query
     * @param sql
     * @param size
     * @return
     */
    private String[] getColumnNames(String sql, int size) throws IOException, SQLException {
        String[] columns = new String[size];

        try (Connection con = connect.getConnection();
             Statement stmt = con.createStatement()) {

            ResultSet res = stmt.executeQuery(sql);
            if(!res.next()) {
                throw new SQLException("No columns whore found");
            }

            int i = 0;
            do {
                columns[i] = res.getString(1);
                i++;

            } while (res.next());
        }

        return columns;

    }

    /**
     * Returns a search query for prepared statment
     * @param sql
     * @param column
     * @return
     */
    public String prepareSearchQuery(String sql, String column){
        return sql + " WHERE " + column + " LIKE ?";
    }

    /**
     * returns a table query to get all elements of a given table
     * @param tableName
     * @param columns
     * @return
     * @throws Exception
     */
    private String prepareTableQuery(String tableName, String[] columns) {
        String finalSQL = "SELECT";

        for (int i = 0; i < columns.length; i++){
            finalSQL += " " + columns[i];

            if(i < columns.length - 1){
                finalSQL += ",";
            }
        }

        finalSQL += " FROM " + tableName;

        return finalSQL;
    }

    /**
     * Formats string based on the format string an object array of column entries.
     * @param string
     * @param formatString
     * @return
     */
    public String formatSingleString(String[] string, String formatString){
        return String.format(formatString, (Object[])string);
    }
}
