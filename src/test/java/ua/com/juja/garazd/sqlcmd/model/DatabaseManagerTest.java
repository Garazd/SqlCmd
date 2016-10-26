package ua.com.juja.garazd.sqlcmd.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.garazd.sqlcmd.controller.properties.ConfigurationTest;
import ua.com.juja.garazd.sqlcmd.controller.properties.Support;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseManagerTest {

    private static ConfigurationTest configurationTest = new ConfigurationTest();
    private static String DATABASE_NAME = configurationTest.getDatabaseNameForTest();
    private static String USER_NAME = configurationTest.getUserNameForTest();
    private static String PASSWORD = configurationTest.getPasswordForTest();
    private static String TEST_TABLE = "test";
    private static String USERS_TABLE = "users";
    private static DatabaseManager manager;
    private static Support support;

    @BeforeClass
    public static void init() {
        manager = new DatabaseManagerImpl();
        support = new Support();
        support.setupData(manager);
    }

    @AfterClass
    public static void clearAfterAllTests() {
        support.dropData(manager);
    }

    @Test
    public void testGetAllTableNames() {
        // given
        // when
        Set<String> tableNames = manager.getTableNames();

        // then
        assertEquals("[users, users2, test]", tableNames.toString());
    }

    @Test
    public void testGetTableData() {
        //given
        manager.clearTable(USERS_TABLE);
        //when
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("id", 4);
        input.put("name", "Stiven");
        input.put("password", "pass");
        manager.createEntry(USERS_TABLE, input);
        //then
        List<Map<String, Object>> users = manager.getTableData(USERS_TABLE);
        assertEquals(1, users.size());

        Map<String, Object> user = users.get(0);
        assertEquals("[4, Stiven, pass]", user.values().toString());
        assertEquals("[id, name, password]", user.keySet().toString());
    }

    @Test
    public void testUpdateTableData() {
        //given
        manager.clearTable(USERS_TABLE);

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("name", "Stiven");
        input.put("password", "pass");
        input.put("id", 5);
        manager.createEntry(USERS_TABLE, input);
        //when
        Map<String, Object> newValue = new LinkedHashMap<>();
        newValue.put("name", "Eva");
        newValue.put("password", "password");
        manager.updateTable(USERS_TABLE, 5, newValue);
        //then
        List<Map<String, Object>> users = manager.getTableData(USERS_TABLE);
        assertEquals(1, users.size());

        Map<String, Object> user = users.get(0);
        assertEquals("[id, name, password]", user.keySet().toString());
        assertEquals("[5, Eva, password]", user.values().toString());
    }

    @Test
    public void testGetColumnNames() {
        //given
        manager.clearTable(USERS_TABLE);

        //when
        Set<String> columnNames = manager.getTableColumns(USERS_TABLE);

        //then
        assertEquals("[id, name, password]", columnNames.toString());
    }

    @Test
    public void testIsConnected() {
        //given
        //when
        //then
        assertTrue(manager.isConnected());
    }

    @Test
    public void testGetTableSize() {
        int size = manager.getTableSize(USERS_TABLE);
        assertEquals(1, size);
    }

    @Test
    public void testClearTable() {
        // given
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("name", "Stiven");
        input.put("password", "pass");
        input.put("id", 17);
        manager.createEntry(USERS_TABLE, input);
        // when
        manager.clearTable(USERS_TABLE);
        // then
        List<Map<String, Object>> users = manager.getTableData(USERS_TABLE);
        assertEquals(0, users.size());
    }

    @Test
    public void testDropTable() {
        manager.dropTable(TEST_TABLE);
        Set<String> tables = manager.getTableNames();
        assertEquals("[users, users2]", tables.toString());
        manager.createTable("test(id SERIAL NOT NULL PRIMARY KEY,username varchar(225) NOT NULL UNIQUE, password varchar(225))");
    }

    @Test
    public void testGetters() {
        assertEquals(DATABASE_NAME, configurationTest.getDatabaseNameForTest());
        assertEquals(USER_NAME, configurationTest.getUserNameForTest());
        assertEquals(PASSWORD, configurationTest.getPasswordForTest());
    }
}