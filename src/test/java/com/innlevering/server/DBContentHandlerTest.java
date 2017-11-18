package com.innlevering.server;

import com.innlevering.server.database.DBContentHandler;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.assertTrue;

/**
 * Created by hakonschutt on 18/11/2017.
 */
public class DBContentHandlerTest {
    private DBContentHandler handler;

    @Before
    public void setUp() throws Exception {
        handler = new DBContentHandler();
    }

    @Test
    public void getAllTablesTest() throws Exception {
        String[] tables = handler.getAllTables();

        assertTrue(Arrays.asList(tables).contains("teacher"));
        assertTrue(Arrays.asList(tables).contains("subject"));
        assertTrue(Arrays.asList(tables).contains("room"));
        assertTrue(Arrays.asList(tables).contains("field_of_study"));
    }

    @Test
    public void getAllColumnsTest() throws Exception {
        String[] columns = handler.getAllColumns("subject");

        assertTrue(Arrays.asList(columns).contains("subject_id"));
        assertTrue(Arrays.asList(columns).contains("subject"));
    }
}
