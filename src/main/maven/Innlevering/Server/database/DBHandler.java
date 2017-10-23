package Innlevering.Server.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by hakonschutt on 23/10/2017.
 */
public class DBHandler {
    private DBConnect connect;

    public DBHandler(){
        connect = new DBConnect();
    }

    public String getTableCountQuery(){
        return "SELECT COUNT(*) as total FROM information_schema.tables WHERE table_schema = '" + connect.getDatabaseName() + "'";
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

    private String prepareQuery(){
        return "SHOW TABLES FROM " + connect.getDatabaseName();
    }

    public String[] getAllTables() throws Exception {
        String sql = prepareQuery();
        String[] tables = new String[getCount(getTableCountQuery())];

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
}
