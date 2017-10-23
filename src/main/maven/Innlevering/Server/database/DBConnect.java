package Innlevering.Server.database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * Main database class. It creates a connection to the database.
 * Created by hakonschutt on 22/10/2017.
 */
public class DBConnect {

    /**
     * Returns a connection to the database.
     * @return
     */
    public Connection getConnection() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("data.properties")) {
            MysqlDataSource ds = new MysqlDataSource();
            properties.load(input);

            ds.setDatabaseName(properties.getProperty("db"));
            ds.setServerName(properties.getProperty("host"));
            ds.setUser(properties.getProperty("user"));
            ds.setPassword(properties.getProperty("pass"));

            Connection connect = ds.getConnection();

            return connect;
        } catch (Exception e){
            return null;
        }
    }

    /**
     * Returns the databasename from the property file.
     * @return
     */
    public String getDatabaseName(){
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("data.properties")) {
            properties.load(input);
            return properties.getProperty("db");
        } catch (Exception e){
            return null;
        }
    }
}
