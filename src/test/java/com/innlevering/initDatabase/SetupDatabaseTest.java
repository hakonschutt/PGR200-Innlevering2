package com.innlevering.initDatabase;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.sql.Connection;

import static org.junit.Assert.assertTrue;

/**
 * To run all tests in this class you need to have ran the setupDatabase
 * class to ensure that we are working with a accepted version.
 * Created by hakonschutt on 18/11/2017.
 */
public class SetupDatabaseTest {

    /**
     * Change all the final fields under to make sure the tests works.
     */
    private final String USER_NAME = "root";
    private final String PASSWORD = "root";
    private final String HOST = "localhost";
    private final String DATABASE = "westerdalsschhak16fortests";

    // DONT CHANGE
    final String LINE_SHIFT = System.getProperty("line.separator");

    @Test
    public void createNewDatabaseTest() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(("no" + LINE_SHIFT + USER_NAME + LINE_SHIFT + PASSWORD + LINE_SHIFT + HOST + LINE_SHIFT + DATABASE + LINE_SHIFT + "3" + LINE_SHIFT).getBytes());
        System.setIn(in);
        new SetupDatabase().startInitOfDatabase();
        System.setIn(System.in);

        Connection con = new SetupConnection().getConnection();

        assertTrue(new DBValidationHandler().validateIfDBExists(con, DATABASE));
    }

    @Test
    public void connectToExistingDatabase() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(("yes" + LINE_SHIFT).getBytes());
        System.setIn(in);
        new SetupDatabase().startInitOfDatabase();
        System.setIn(System.in);

        Connection con = new SetupConnection().getConnection();

        assertTrue(new DBValidationHandler().validateIfDBExists(con, DATABASE));
    }
}
