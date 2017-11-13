package innlevering.server.database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
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
    public Connection getConnection() throws IOException, SQLException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("data.properties")) {
            MysqlDataSource ds = new MysqlDataSource();
            properties.load(input);

            ds.setUser(properties.getProperty("user"));
            ds.setPassword(properties.getProperty("pass"));
            ds.setDatabaseName(properties.getProperty("db"));
            ds.setServerName(properties.getProperty("host"));

            Connection connect = ds.getConnection();

            return connect;
        } catch (FileNotFoundException e){
            throw new FileNotFoundException("Unable to locate property file.");
        }
    }

    /**
     * Returns the databasename from the property file.
     * @return
     */
    public String getDatabaseName() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("data.properties")) {
            properties.load(input);
            return properties.getProperty("db");
        } catch (FileNotFoundException e){
            System.out.println("Unable to locate property file.");
            return null;
        }
    }
}
