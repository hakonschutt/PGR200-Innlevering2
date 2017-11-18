package com.innlevering.server;

import com.innlevering.server.database.DBValidator;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by hakonschutt on 18/11/2017.
 */
public class DBValidatorTest {
    private DBValidator val;
    private String currentDB;

    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();
        InputStream input = new FileInputStream("data.properties");
        properties.load(input);
        this.currentDB = properties.getProperty("db");
        val = new DBValidator();
    }

    @Test
    public void getAllTablesTest() throws Exception {
        String[] tables = val.getAllTables();

        assertTrue(Arrays.asList(tables).contains("room"));
        assertTrue(Arrays.asList(tables).contains("field_of_study"));
        assertTrue(Arrays.asList(tables).contains("teacher"));
        assertTrue(Arrays.asList(tables).contains("subject"));
    }

    @Test
    public void getDatabaseNameTest() throws Exception {
        String database = val.getDatabaseName();
        assertEquals(currentDB, database);
    }

    @Test
    public void startDatabaseCheckTest() throws Exception {
        assertTrue(new DBValidator().startDatabaseCheck());
    }
}
