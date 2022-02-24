package csci310.utilities;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class BlockedListDatabaseTest {

    @Before
    public void setUp() throws Exception {
        BlockedListDatabase blockDB = new BlockedListDatabase();
    }

    @Test
    public void testCreateDatabase() throws SQLException {
        BlockedListDatabase.CreateDatabase();
        assertEquals(false, BlockedListDatabase.IsBlocking("test1", "test2"));
        BlockedListDatabase.AddBlock("test1", "test2");
        assertEquals(true, BlockedListDatabase.IsBlocking("test1", "test2"));
        BlockedListDatabase.DeleteDatabase();
    }

    @Test
    public void testDeleteDatabase() throws SQLException {
        BlockedListDatabase.CreateDatabase();
        assertEquals(false, BlockedListDatabase.IsBlocking("test1", "test2"));
        BlockedListDatabase.AddBlock("test1", "test2");
        assertEquals(true, BlockedListDatabase.IsBlocking("test1", "test2"));
        BlockedListDatabase.DeleteDatabase();
        assertEquals(false, BlockedListDatabase.IsBlocking("test1", "test2"));
    }

    @Test
    public void testAddBlock() throws SQLException {
        BlockedListDatabase.CreateDatabase();
        assertEquals(false, BlockedListDatabase.IsBlocking("test1", "test2"));
        BlockedListDatabase.AddBlock("test1", "test2");
        assertEquals(true, BlockedListDatabase.IsBlocking("test1", "test2"));
        BlockedListDatabase.AddBlock("a", "test2");
        assertEquals(false, BlockedListDatabase.IsBlocking("a", "test2"));
        BlockedListDatabase.AddBlock("test1", "b");
        assertEquals(false, BlockedListDatabase.IsBlocking("test1", "b"));
        BlockedListDatabase.DeleteDatabase();
    }

    @Test
    public void testDeleteBlock() throws SQLException {
        BlockedListDatabase.CreateDatabase();
        assertEquals(false, BlockedListDatabase.IsBlocking("test1", "test2"));
        BlockedListDatabase.AddBlock("test1", "test2");
        assertEquals(true, BlockedListDatabase.IsBlocking("test1", "test2"));
        BlockedListDatabase.DeleteBlock("test1", "test2");
        assertEquals(false, BlockedListDatabase.IsBlocking("test1", "test2"));
        BlockedListDatabase.DeleteDatabase();
    }

    @Test
    public void testGetBlockedList() throws SQLException {
        BlockedListDatabase.CreateDatabase();
        ArrayList<String> blockList = new ArrayList<String>();
        assertEquals(true, blockList.equals(BlockedListDatabase.GetBlockedList("test1")));

        BlockedListDatabase.AddBlock("test1", "test2");
        blockList.add("test2");
        assertEquals(true, blockList.equals(BlockedListDatabase.GetBlockedList("test1")));
        assertEquals(false, blockList.equals(BlockedListDatabase.GetBlockedList("test2")));
        assertEquals(false, blockList.equals(BlockedListDatabase.GetBlockedList("test3")));

        blockList.add("test3");
        BlockedListDatabase.AddBlock("test1", "test3");
        assertEquals(true, blockList.equals(BlockedListDatabase.GetBlockedList("test1")));

        blockList.remove("test2");
        BlockedListDatabase.DeleteBlock("test1", "test2");
        assertEquals(true, blockList.equals(BlockedListDatabase.GetBlockedList("test1")));

        BlockedListDatabase.DeleteDatabase();

    }

    @Test
    public void testIsBlocking() throws SQLException {
        BlockedListDatabase.CreateDatabase();
        assertEquals(false, BlockedListDatabase.IsBlocking("test1", "test2"));
        BlockedListDatabase.AddBlock("test1", "test2");
        assertEquals(true, BlockedListDatabase.IsBlocking("test1", "test2"));
        BlockedListDatabase.DeleteDatabase();
    }
}