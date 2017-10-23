package Innlevering.Server.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Created by hakonschutt on 23/10/2017.
 */
public class DBHandler {
    private DBConnect connect;

    public DBHandler(){
        connect = new DBConnect();
    }

    public int getCount(String sql) throws Exception{
        try (Connection con = connect.getConnection();
             Statement stmt = con.createStatement()) {
            ResultSet res = stmt.executeQuery(sql);
            if(!res.next()) {
                throw new SQLException("No tables where found");
            }
            return res.getInt("total");
        } catch (SQLException e ){
            throw new SQLException("Unable to connect with current connection");
        }
    }

    public int getSearchCount(String sql, String searchString) throws Exception{

        try (Connection con = connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, searchString);
            ResultSet res = ps.executeQuery();
            if(!res.next()) {
                throw new SQLException("No tables where found");
            }
            return res.getInt("total");
        } catch (SQLException e) {
            return -1;
        }
    }

    public String getTableEntriesCountFromSearch(String tableName, String columnName){
        return "SELECT COUNT(*) as total FROM " + tableName + " WHERE " + columnName + " LIKE ?";
    }

    public String getTableCountQuery(){
        return "SELECT COUNT(*) as total FROM information_schema.tables WHERE table_schema = '" + connect.getDatabaseName() + "'";
    }

    public String getColumnCountQuery( String tableName ){
        return "SELECT COUNT(*) as total " +
                "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'" +
                tableName + "' AND table_schema = '" + connect.getDatabaseName() + "'";
    }

    public String getTableEntriesCount( String tableName ){
        return "SELECT COUNT(*) as total FROM " + tableName;
    }

    public String prepareQuery(){
        return "SHOW TABLES FROM " + connect.getDatabaseName();
    }

    public String prepareColumnDataQuery( String tableName ) throws Exception {
        String sql = "SELECT COLUMN_NAME " +
                "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'" +
                tableName + "' AND table_schema = '" + connect.getDatabaseName() + "'";

        return sql;
    }
}
