package Innlevering.Server.database;

import Innlevering.Server.handlers.FormatHandler;

import java.sql.*;

/**
 * Created by hakonschutt on 23/10/2017.
 */
public class DBHandlerData {
    private DBHandler handler;
    private FormatHandler formatHandler;
    private DBConnect connect;

    public DBHandlerData() {
        formatHandler = new FormatHandler();
        handler = new DBHandler();
        connect = new DBConnect();
    }

    public String[] getAllTables() throws Exception {
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

    public String[] getAllColumns(String tableName) throws Exception {
        int size = handler.getCount( handler.getColumnCountQuery( tableName ) );
        String query = handler.prepareColumnDataQuery( tableName );
        String[] columns = getColumnNames(query, size);

        return columns;
    }

    public String[] getSearchContent(String searchString, String[] columns, String chosenColumn, String tableName) throws Exception {
        String format = formatHandler.getFormatFromHandler(tableName);
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

    public String[] getTableContent(String tableName) throws Exception {
        String format = formatHandler.getFormatFromHandler(tableName);
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

    private String[] getAllTableContentFromSearch(String finalQuery, String[] columns, int entrySize, String format, String searchString){
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataEntries;
    }

    private String[] getAllTableContent(String sql, String[] columns, int entrySize, String format){
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataEntries;
    }

    private String[] getColumnNames(String sql, int size){
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
            e.getSQLState();
        }
        return columns;

    }

    public String prepareSearchQuery(String sql, String column){
        return sql + " WHERE " + column + " LIKE ?";
    }

    private String prepareTableQuery(String tableName, String[] columns) throws Exception {
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

    public String formatSingleString(String[] string, String formatString){
        return String.format(formatString, (Object[])string);
    }
}
