package Innlevering.Server.database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * Created by hakonschutt on 22/10/2017.
 */
public class DBConnect {
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
}
