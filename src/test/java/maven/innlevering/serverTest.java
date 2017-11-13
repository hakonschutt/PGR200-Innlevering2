package maven.innlevering;

import innlevering.server.database.DBConnect;
import innlevering.server.database.DBHandler;
import innlevering.server.handlers.ThreadHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by hakonschutt on 23/10/2017.
 */
public class serverTest {
    private DBConnect connect;
    private DBHandler handler;
    private ThreadHandler threadHandler;

    @Before
    public void setUp() throws Exception {
        connect = new DBConnect();
        handler = new DBHandler();
        threadHandler = new ThreadHandler();
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void checkConnection() throws Exception {
        Connection con = connect.getConnection();

        assertNotNull(con);
    }

    @Test
    public void testGetCount() throws Exception {
        String sql = "SELECT COUNT(*) as total FROM room";

        try {
            assertEquals(handler.getCount(sql), 4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPrepateTable() throws Exception {
        String[] ent = {"test", "hello", "world", "!!"};
        int userchoice = 2;

        assertEquals(threadHandler.getElementFromUserInput(ent, userchoice), "hello");
    }
}