package Innlevering.Server.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by hakonschutt on 23/10/2017.
 */
public class DBHandlerTableData {
    private DBConnect connect;
    private DBHandler handler;

    public DBHandlerTableData() {
        connect = new DBConnect();
        handler = new DBHandler();
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
}
