package com.innlevering.initDatabase;

import com.innlevering.initDatabase.exception.InitDBFileNotFoundException;
import com.innlevering.initDatabase.exception.InitDBSQLException;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by hakonschutt on 18/11/2017.
 */
public class SetupConnectionTest {

    @Test(expected = InitDBSQLException.class)
    public void verifyConnectionWithUserInputTest_ThrowSQLException() throws Exception {
        SetupConnection connect = new SetupConnection("hello", "hello", "notlocalhost", "westerdals");
        Connection con = connect.verifyConnectionWithUserInput(true);
    }

    @Test
    public void verifyConnectionWithUserInputTest() throws Exception {
        Properties properties = new Properties();
        InputStream input = new FileInputStream("data.properties");
        properties.load(input);

        String user = properties.getProperty("user");
        String pass = properties.getProperty("pass");
        String host = properties.getProperty("host");
        String db = properties.getProperty("db");

        assertNotNull(user);
        assertNotNull(pass);
        assertNotNull(host);
        assertNotNull(db);

        SetupConnection connect = new SetupConnection(user, pass, host, db);
        Connection con = connect.verifyConnectionWithUserInput(true);

        assertNotNull(con);
    }

    @Test
    public void getConnectionTest() throws Exception {
        SetupConnection connect = new SetupConnection();
        Connection con = connect.getConnection();

        assertNotNull(con);
    }

    @Test
    public void getConnectionTest_withoutProperties() throws Exception {
        assertTrue(new File("data.properties").renameTo(new File("temp.properties")));

        try {
            SetupConnection connection = new SetupConnection();
            Connection con = connection.getConnection();

        } catch (InitDBFileNotFoundException e){
            assertEquals(e.getMessage(), "Unable to locate property file. Make sure its not deleted.");

            assertTrue(new File("temp.properties").renameTo(new File("data.properties")));
        }
    }
}
