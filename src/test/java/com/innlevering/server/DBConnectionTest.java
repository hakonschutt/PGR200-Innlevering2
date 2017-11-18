package com.innlevering.server;

import com.innlevering.exception.ServerFileNotFoundException;
import com.innlevering.server.database.DBConnection;
import org.junit.Test;
import java.io.File;
import java.sql.Connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by hakonschutt on 18/11/2017.
 */
public class DBConnectionTest {

    @Test
    public void getConnectionTest() throws Exception {
        DBConnection connect = new DBConnection();
        Connection con = connect.getConnection();

        assertNotNull(con);
    }

    @Test
    public void getConnectionTest_withoutProperties() throws Exception {
        assertTrue(new File("data.properties").renameTo(new File("temp.properties")));

        try {
            DBConnection connection = new DBConnection();
            Connection con = connection.getConnection();

        } catch (ServerFileNotFoundException e){
            assertEquals(e.getMessage(), "Unable to locate property file. Make sure its not deleted.");

            assertTrue(new File("temp.properties").renameTo(new File("data.properties")));
        }
    }
}
