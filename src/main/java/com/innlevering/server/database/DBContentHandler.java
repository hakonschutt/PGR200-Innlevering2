package com.innlevering.server.database;

import com.innlevering.exception.ServerFileNotFoundException;
import com.innlevering.exception.ServerIOException;
import com.innlevering.exception.ServerSQLException;
import com.innlevering.server.handlers.FormatHandler;
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
     * @throws ServerFileNotFoundException
     * @throws ServerIOException
     * @throws ServerSQLException
     */
    public String[] getAllTables() throws ServerFileNotFoundException, ServerIOException, ServerSQLException {
        String sql = handler.prepareQuery();
        String[] tables = new String[handler.getCount(handler.getTableCountQuery())];

        try (Connection con = connect.getConnection();
             Statement stmt = con.createStatement()) {
            int i = 0;
            ResultSet res = stmt.executeQuery(sql);

            if(!res.next()) return new String[0];

            do {
                tables[i] = res.getString(1);
                i++;
            } while (res.next());
        } catch (SQLException e){
            throw new ServerSQLException(ServerSQLException.getErrorMessage("tables"));
        }

        return tables;
    }

    /**
     * returns all the columns in a given table
     * @param tableName
     * @return
     * @throws ServerFileNotFoundException
     * @throws ServerIOException
     * @throws ServerSQLException
     */
    public String[] getAllColumns(String tableName) throws ServerFileNotFoundException, ServerIOException, ServerSQLException {
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
     * @throws ServerFileNotFoundException
     * @throws ServerIOException
     * @throws ServerSQLException
     */
    public String[] getSearchContent(String searchString, String[] columns, String chosenColumn, String tableName) throws ServerFileNotFoundException, ServerIOException, ServerSQLException {
        String format = FormatHandler.getFormatFromHandler(tableName);
        int entrySize = handler.getSearchCount(handler.getTableEntriesCountFromSearch( tableName, chosenColumn ), searchString);
        String[] data;

        if(entrySize > 0){
            data = new String[entrySize + 4];
            String tempQuery = prepareTableQuery(tableName, columns);
            String finalQuery = prepareSearchQuery(tempQuery, chosenColumn);

            String[] tempData = getAllTableContentFromSearch(finalQuery, columns, entrySize, format, searchString);

            String intro = formatSingleString(columns, format);
            String seperator = getLineBreak(intro);

            data[0] = seperator;
            data[1] = intro;
            data[2] = seperator;

            for (int i = 3; i < data.length - 1; i++){
                data[i] = tempData[i-3];
            }

            data[data.length - 1] = seperator;
        } else {
            data = new String[]{"No data was found with this search!"};
        }

        return data;
    }

    /**
     * Returns a string of - with the length of a given string
     * @param datum
     * @return
     */
    private String getLineBreak(String datum) {
        String finalString = "";

        for(int i = 0; i < datum.length(); i++){
            finalString += "-";
        }

        return finalString;
    }

    /**
     * Returns all table content for a given table
     * @param tableName
     * @return
     * @throws ServerFileNotFoundException
     * @throws ServerIOException
     * @throws ServerSQLException
     */
    public String[] getTableContent(String tableName) throws ServerFileNotFoundException, ServerIOException, ServerSQLException {
        String format = FormatHandler.getFormatFromHandler(tableName);
        int entrySize = handler.getCount(handler.getTableEntriesCount( tableName ));

        String[] data = new String[entrySize + 4];
        int size = handler.getCount( handler.getColumnCountQuery( tableName ) );
        String query = handler.prepareColumnDataQuery( tableName );

        String[] columns = getColumnNames(query, size);
        String finalQuery = prepareTableQuery(tableName, columns);
        String[] tempData = getAllTableContent(finalQuery, columns, entrySize, format);

        String intro = formatSingleString(columns, format);
        String seperator = getLineBreak(intro);

        data[0] = seperator;
        data[1] = intro;
        data[2] = seperator;

        for (int i = 3; i < data.length - 1; i++){
            data[i] = tempData[i-3];
        }

        data[data.length - 1] = seperator;

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
     * @throws ServerFileNotFoundException
     * @throws ServerIOException
     * @throws ServerSQLException
     */
    private String[] getAllTableContentFromSearch(String finalQuery, String[] columns, int entrySize, String format, String searchString) throws ServerFileNotFoundException, ServerIOException, ServerSQLException {
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
        } catch (SQLException e){
            throw new ServerSQLException(ServerSQLException.getErrorMessage("tableContent"));
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
     * @throws ServerFileNotFoundException
     * @throws ServerIOException
     * @throws ServerSQLException
     */
    private String[] getAllTableContent(String sql, String[] columns, int entrySize, String format) throws ServerFileNotFoundException, ServerIOException, ServerSQLException {
        String[] dataEntries = new String[entrySize];

        try (Connection con = connect.getConnection();
             Statement stmt = con.createStatement()) {
            ResultSet res = stmt.executeQuery(sql);

            if(!res.next()) return new String[0];

            int i = 0;

            do {
                String[] tempData = new String[columns.length];
                for(int j = 0; j < columns.length; j++){
                    tempData[j] = String.valueOf(res.getObject(columns[j]));
                }
                dataEntries[i] = formatSingleString(tempData, format);
                i++;

            } while (res.next());
        } catch (SQLException e){
            throw new ServerSQLException(ServerSQLException.getErrorMessage("tableContent"));
        }

        return dataEntries;
    }

    /**
     * Returns all column names from a given query
     * @param sql
     * @param size
     * @return
     * @throws ServerFileNotFoundException
     * @throws ServerIOException
     * @throws ServerSQLException
     */
    private String[] getColumnNames(String sql, int size) throws ServerFileNotFoundException, ServerIOException, ServerSQLException {
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
        } catch (SQLException e){
            throw new ServerSQLException(ServerSQLException.getErrorMessage("columns"));
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
