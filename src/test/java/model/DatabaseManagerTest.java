package model;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public abstract class DatabaseManagerTest {

    private DatabaseManager manager;

    public abstract DatabaseManager getDatabaseManager();

    @Before
    public void setup() {
        manager = getDatabaseManager();
        manager.connect("sqlcmd", "postgres", "postgres");
    }

    @Test
    public void testGetAllTableNames() {
        String[] tableNames = manager.getTableNames();
        assertEquals("[user, test]", Arrays.toString(tableNames));
    }

    @Test
    public void testGetTableData() {
        //given
        manager.clear("user");

        //when
        DataSet input = new DataSet();
        input.put("name", "Vitaliy");
        input.put("password", "pass");
        input.put("id", 4);
        manager.create("user", input);

        //then
        DataSet[] users = manager.getTableData("user");
        assertEquals(1, users.length);

        DataSet user = users[0];
        assertEquals("[name, password, id]", Arrays.toString(user.getNames()));
        assertEquals("[Vitaliy, pass, 4]", Arrays.toString(user.getValues()));
    }

    @Test
    public void testUpdateTableData() {
        //given
        manager.clear("user");

        DataSet input = new DataSet();
        input.put("name", "Vitaliy");
        input.put("password", "pass");
        input.put("id", 4);
        manager.create("user", input);

        //when
        DataSet newValue = new DataSet();
        newValue.put("password", "pass2");
        newValue.put("name", "Garazd");
        manager.update("user", 4, newValue);

        //then
        DataSet[] users = manager.getTableData("user");
        assertEquals(1, users.length);

        DataSet user = users[0];
        assertEquals("[name, password, id]", Arrays.toString(user.getNames()));
        assertEquals("[Garazd, pass2, 4]", Arrays.toString(user.getValues()));
    }

    @Test
    public void testGetColumnNames() {
        //given
        manager.clear("user");

        //when
        String[] columnNames = manager.getTableColumns("user");

        //then
        assertEquals("[name, password, id]", Arrays.toString(columnNames));
    }

    @Test
    public void testIsConnected() {
        //given
        //when
        //then
        assertTrue(manager.isConnected());
    }
}