package com.innlevering.server;

import com.innlevering.server.database.DBHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by hakonschutt on 23/10/2017.
 */
public class DBHandlerTest {
    private DBHandler handler;

    @Before
    public void setUp() throws Exception {
        handler = new DBHandler();
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testGetCount() throws Exception {
        String sql = "SELECT COUNT(*) as total FROM room";

        try {
            assertEquals(handler.getCount(sql), 4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}