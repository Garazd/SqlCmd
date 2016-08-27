package ua.com.juja.garazd.sqlcmd.model;

import java.util.List;
import java.util.Set;
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
        manager.connectDatabase("sqlcmd", "postgres", "postgres");
    }

    @Test
    public void testGetAllTableNames() {
        // given
        manager.getTableData("user");
        manager.getTableData("test");

        // when
        Set<String> tableNames = manager.getTableNames();

        // then
        assertEquals("[user, test]", tableNames.toString());
    }

    @Test
    public void testGetTableData() {
        //given
        manager.clearTable("user");

        //when
        DataSet input = new DataSetImpl();
        input.put("name", "Stiven");
        input.put("password", "pass");
        input.put("id", 4);
        manager.createTable("user", input);

        //then
        List<DataSet> users = manager.getTableData("user");
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[name, password, id]", user.getNames().toString());
        assertEquals("[Stiven, pass, 4]", user.getValues().toString());
    }

    @Test
    public void testUpdateTableData() {
        //given
        manager.clearTable("user");

        DataSet input = new DataSetImpl();
        input.put("name", "Stiven");
        input.put("password", "pass");
        input.put("id", 4);
        manager.createTable("user", input);

        //when
        DataSetImpl newValue = new DataSetImpl();
        newValue.put("password", "pass2");
        newValue.put("name", "Eva");
        manager.updateTable("user", 4, newValue);

        //then
        List<DataSet> users = manager.getTableData("user");
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[name, password, id]", user.getNames().toString());
        assertEquals("[Eva, pass2, 4]", user.getValues().toString());
    }

    @Test
    public void testGetColumnNames() {
        //given
        manager.clearTable("user");

        //when
        Set<String> columnNames = manager.getTableColumns("user");

        //then
        assertEquals("[name, password, id]", columnNames.toString());
    }

    @Test
    public void testIsConnected() {
        //given
        //when
        //then
        assertTrue(manager.isConnected());
    }
}