package innlevering.server.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * This class validates if all the necassary classes are present for the server to run.
 * Created by hakonschutt on 22/10/2017.
 */
public class DBValidator {
    private DBConnect connect = new DBConnect();

    /**
     * Tests if connection works.
     * gets all the tables and returns true if the validateTables is true. else it returns false.
     * @return
     */
    public boolean startDatabaseCheck () throws IOException, SQLException {
        try (Connection con = connect.getConnection()){
            String[] tables = getAllTables();
            return validateTables( tables );
        }
    }

    /**
     * Gets a list of tables, and checks if they are the same as what the program needs.
     * @param tables
     * @return
     */
    private boolean validateTables(String[] tables) {
        int count = 0;
        for(int i = 0; i < tables.length; i++){
            if(tables[i].equals("field_of_study") || tables[i].equals("room") || tables[i].equals("subject") || tables[i].equals("teacher")){
                count++;
            }
        }
        return count == 4;
    }

    /**
     * Returns the database name in the property file.
     * @return
     * @throws IOException
     */
    private String getDatabaseName() throws IOException {
        Properties properties = new Properties();
        InputStream input = new FileInputStream("data.properties");
        properties.load(input);

        return properties.getProperty("db");
    }

    /**
     * Returns an array with all the tables in the database
     * @return
     * @throws Exception
     */
    public String[] getAllTables() throws IOException, SQLException {
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
        }

        return tables;
    }
}
