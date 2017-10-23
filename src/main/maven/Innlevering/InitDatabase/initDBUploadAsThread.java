package Innlevering.InitDatabase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/**
 * Created by hakonschutt on 22/10/2017.
 */
public class initDBUploadAsThread implements Runnable {
    private String file;
    private InitDBConnect db = new InitDBConnect();

    public initDBUploadAsThread(String file) {
        this.file = file;
    }

    /**
     * Thread run job method. Calls all methods used by thread.
     */
    @Override
    public void run() {
        try {
            createQuery(file);
            insertQuery(file);
            System.out.println("Finished importing " + file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates query to create table from the input file
     * @param fileName
     * @throws IOException
     */
    private void createQuery(String fileName) throws IOException {
        try{
            String file = "input/" + fileName;
            BufferedReader in = new BufferedReader(new FileReader(file));

            String table = in.readLine();
            String[] col = in.readLine().split("/");
            String[] dataType = in.readLine().split("/");
            String[] dataSize = in.readLine().split("/");
            String PK = in.readLine();

            String sql;

            sql = "CREATE TABLE `" + table + "` ( ";
            for(int i = 0; i < col.length; i++){
                sql += "`" + col[i] + "` " + dataType[i] + "(" + dataSize[i] + ") NOT NULL,";
            }
            sql += " PRIMARY KEY (`" + PK + "`))";

            executeCreate(sql);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * General execute create query that is used to create tables
     * @param sql
     */
    private void executeCreate(String sql){
        try (Connection con = db.getConnection();
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates insert Query from the current file
     * @param filename
     * @throws IOException
     */
    private void insertQuery(String filename) throws IOException {
        String file = "input/" + filename;
        BufferedReader in = new BufferedReader(new FileReader(file));

        String db = in.readLine();
        String[] col = in.readLine().split("/");
        String[] dataType = in.readLine().split("/");
        String[] dataSize = in.readLine().split("/");
        String PK = in.readLine();

        String sql;

        String s;
        while((s = in.readLine()) != null){
            sql = "INSERT INTO `" + db + "` ( ";
            for(int i = 0; i < col.length; i++){
                if(i == col.length -1){
                    sql += "`" + col[i] + "`";
                } else {
                    sql += "`" + col[i] + "`, ";
                }
            }
            sql += ") VALUES ( ";

            String[] var = s.split("/");

            for(int i = 0; i < var.length; i++){
                if(i == col.length -1){
                    sql += "? ";
                } else {
                    sql += "?, ";
                }
            }
            sql += ")";

            executeInsert(sql, var);
        }
    }

    /**
     * Executes insert query for table information.
     * @param sql
     * @param var
     */
    private void executeInsert(String sql, String[] var){
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < var.length; i++){
                ps.setObject(i + 1, var[i]);
            }
            int rs = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println();
        }
    }
}
