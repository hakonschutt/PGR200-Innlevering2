package Innlevering.Server.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by hakonschutt on 22/10/2017.
 */
public class DatabaseValidation {
    private DBConnect connect = new DBConnect();

    public boolean startDatabaseCheck () {
        try (Connection con = connect.getConnection()){
            String[] tables = getAlleTables();
            return validateTables( tables );
        } catch (Exception e){
            System.out.println("Unable to connection with property entry...");
            return false;
        }
    }

    private boolean validateTables(String[] tables) {
        int count = 0;
        for(int i = 0; i < tables.length; i++){
            if(tables[i].equals("field_of_study") || tables[i].equals("room") || tables[i].equals("subject") || tables[i].equals("teacher")){
                count++;
            }
        }
        return count == 4;
    }

    private String getDatabaseName() throws IOException {
        Properties properties = new Properties();
        InputStream input = new FileInputStream("data.properties");
        properties.load(input);

        return properties.getProperty("db");
    }

    public String[] getAlleTables() throws Exception{
        String sql = "SHOW TABLES FROM " + getDatabaseName();
        String[] tables = new String[8];

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
