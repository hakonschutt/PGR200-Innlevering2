package innlevering.initDatabase;

import java.io.*;
import java.sql.*;
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

    /**
     * Method is used to return all tables in a String array format
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public String[] getAllTables(Connection con) throws IOException, SQLException {
        String[] files = new FileUploadHandler().getAllFiles();

        String database = getDatabaseName();
        String sql = "SHOW TABLES FROM " + database;

        String[] tables = new String[files.length];

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
        }

        return tables;
    }

    /**
     * Deletes the database if the user wants to overwrite the current database
     * @param con
     * @param dbName
     * @throws SQLException
     */
    public void overWriteDatabase( Connection con, String dbName ) throws SQLException {
        try ( Statement stmt = con.createStatement() ){
            stmt.executeUpdate("DROP DATABASE " + dbName +  "");
            createDataBase( con, dbName );
        }
    }

    /**
     * Creates a new database if the user has entered a new database name or want to overwrite the current database
     * @param con
     * @param newDbName
     * @throws SQLException
     */
    public void createDataBase( Connection con, String newDbName ) throws SQLException {
        try (Statement stmt = con.createStatement()){
            stmt.executeUpdate("CREATE DATABASE " + newDbName +  "");
        }
    }

    /**
     * Validates if the database already exists.
     * @param con
     * @param databaseName
     * @return
     * @throws SQLException
     */
    public boolean validateIfDBExists( Connection con, String databaseName ) throws SQLException {
        try (Statement stmt = con.createStatement();
             ResultSet res =
                     stmt.executeQuery(
                             "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '"
                                     + databaseName + "'")){

            return res.next();
        }
    }

    /**
     * Fixes foreign keys on table so everything works after thread has run the upload.
     * @param fileName
     * @throws IOException
     * @throws SQLException
     */
    public void fixForeignKeysForTable(String fileName) throws IOException, SQLException {
        String file = "input/" + fileName;

        try (BufferedReader in = new BufferedReader(new FileReader(file))){
            String table = in.readLine();

            for (int i = 0; i < 4; i++) in.readLine();

            String[] FK = in.readLine().split("/");

            if(!FK[0].equals("nofk")){
                String sql;

                sql = "ALTER TABLE `" + table + "` ";
                for(int i = 0; i < FK.length; i+=3){
                    sql += "ADD FOREIGN KEY (" + FK[i] + ") REFERENCES " + FK[i + 1] + "(" + FK[i + 2] + ")";
                    if(i == FK.length - 3){
                        sql += "";
                    } else {
                        sql += ", ";
                    }
                }

                executeUpdate(sql);
            }
        }
    }

    /**
     * Executes the alter that fixes the foreign key issue.
     * @param sql
     * @throws IOException
     * @throws SQLException
     */
    public void executeUpdate(String sql) throws IOException, SQLException {
        SetupConnection db = new SetupConnection();
        try (Connection con = db.getConnection();
             Statement stmt = con.createStatement()){
            stmt.executeUpdate(sql);
        }
    }

    public Boolean getScanned() {
        return isScanned;
    }
}
