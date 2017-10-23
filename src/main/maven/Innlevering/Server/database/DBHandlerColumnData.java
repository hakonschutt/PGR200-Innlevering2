package Innlevering.Server.database;

import Innlevering.Server.handlers.FormatHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by hakonschutt on 23/10/2017.
 */
public class DBHandlerColumnData {
    private DBHandler handler;
    private FormatHandler formatHandler;
    private DBConnect connect;

    public DBHandlerColumnData() {
        formatHandler = new FormatHandler();
        handler = new DBHandler();
        connect = new DBConnect();
    }

    public String[] getTableContent(String tableName) throws Exception {
        String format = getFormatFromHandler(tableName);
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

    public String getFormatFromHandler(String tableName){
        switch (tableName ){
            case "day_teacher_unavailability":
                return formatHandler.getDay_teacher_unavailability();
            case "field_of_study":
                return formatHandler.getField_of_study();
            case "possible_day":
                return formatHandler.getPossible_day();
            case "room":
                return formatHandler.getRoom();
            case "study_subject":
                return formatHandler.getStudy_subject();
            case "subject":
                return formatHandler.getSubject();
            case "teacher":
                return formatHandler.getTeacher();
            case "teacher_subject":
                return formatHandler.getTeacher_subject();
        }
        return null;
    }
}
