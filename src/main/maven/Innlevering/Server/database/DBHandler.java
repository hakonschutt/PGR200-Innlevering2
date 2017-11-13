package innlevering.server.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Main database assist class.
 * Creates queries for the program, and execute count queries.
 * Created by hakonschutt on 23/10/2017.
 */
public class DBHandler {
    private DBConnect connect;

    /**
     * Sets a connection to the DBConnect class
     */
    public DBHandler(){
        connect = new DBConnect();
    }

    /**
     * Returns the amount of entries based on the query
     * @param sql
     * @return
     * @throws Exception
     */
    public int getCount(String sql) throws IOException, SQLException {
        try (Connection con = connect.getConnection();
             Statement stmt = con.createStatement()) {
            ResultSet res = stmt.executeQuery(sql);
            if(!res.next()) {
                throw new SQLException("No tables where found");
            }
            return res.getInt("total");
        }
    }

    /**
     * Returns the amount of entries based on the preparedStatement.
     * @param sql
     * @param searchString
     * @return
     * @throws Exception
     */
    public int getSearchCount(String sql, String searchString) throws IOException, SQLException {

        try (Connection con = connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, searchString);
            ResultSet res = ps.executeQuery();
            if(!res.next()) {
                throw new SQLException("No tables where found");
            }
            return res.getInt("total");
        }
    }

    /**
     * Returns a query that can be used for getting the entries in a search
     * @param tableName
     * @param columnName
     * @return
     */
    public String getTableEntriesCountFromSearch(String tableName, String columnName){
        return "SELECT COUNT(*) as total FROM " + tableName + " WHERE " + columnName + " LIKE ?";
    }

    /**
     * Returns a query that can be used t check the number of tables in a database.
     * @return
     */
    public String getTableCountQuery() throws IOException {
        return "SELECT COUNT(*) as total FROM information_schema.tables WHERE table_schema = '" + connect.getDatabaseName() + "'";
    }

    /**
     * Returns a query that can be used to check the number of columns in a table
     * @param tableName
     * @return
     */
    public String getColumnCountQuery( String tableName ) throws IOException {
        return "SELECT COUNT(*) as total " +
                "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'" +
                tableName + "' AND table_schema = '" + connect.getDatabaseName() + "'";
    }

    /**
     * Returns a query that can check how man entries there are in a table
     * @param tableName
     * @return
     */
    public String getTableEntriesCount( String tableName ){
        return "SELECT COUNT(*) as total FROM " + tableName;
    }

    /**
     * Returns a query that can be used to get alle tables in a database
     * @return
     */
    public String prepareQuery() throws IOException {
        return "SHOW TABLES FROM " + connect.getDatabaseName();
    }

    /**
     * Returns a query that can be used to get all columns in a table
     * @param tableName
     * @return
     * @throws Exception
     */
    public String prepareColumnDataQuery( String tableName ) throws IOException {
        String sql = "SELECT COLUMN_NAME " +
                "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'" +
                tableName + "' AND table_schema = '" + connect.getDatabaseName() + "'";

        return sql;
    }
}
