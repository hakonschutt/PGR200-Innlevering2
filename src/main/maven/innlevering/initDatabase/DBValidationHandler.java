package innlevering.initDatabase;

import java.io.IOException;
import java.sql.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by hakonschutt on 22/10/2017.
 */
public class DBValidationHandler {
    private Boolean isScanned = true;

    private String getDatabaseName() throws IOException {
        Properties properties = new Properties();
        InputStream input = new FileInputStream("data.properties");
        properties.load(input);

        return properties.getProperty("db");
    }

    public String[] getAlleTables(Connection con) throws Exception{
        String database = getDatabaseName();
        String sql = "SHOW TABLES FROM " + database;

        String[] tables = new String[8];

        try (Statement stmt = con.createStatement()) {
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

    public void overWriteDatabase( Connection con, String dbName ){
        try ( Statement stmt = con.createStatement() ){
            stmt.executeUpdate("DROP DATABASE " + dbName +  "");
            createDataBase( con, dbName );
        } catch ( SQLException e ){
            System.out.println("Unable to drop table.");
        }
    }

    public void createDataBase( Connection con, String newDbName ){
        try (Statement stmt = con.createStatement()){
            stmt.executeUpdate("CREATE DATABASE " + newDbName +  "");
            setScanned( false );
        } catch ( SQLException e ){
            System.out.println("Unable to create new database.");
        }
    }

    public boolean validateIfDBExists( Connection con, String databaseName ) throws Exception {
        try (Statement stmt = con.createStatement();
             ResultSet res =
                     stmt.executeQuery(
                             "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '"
                                     + databaseName + "'")){

            return res.next();
        } catch ( SQLException e ){
            System.out.println("Unable to validate if database exists");
            return false;
        }
    }

    public Boolean getScanned() {
        return isScanned;
    }

    public void setScanned(Boolean scanned) {
        isScanned = scanned;
    }
}
