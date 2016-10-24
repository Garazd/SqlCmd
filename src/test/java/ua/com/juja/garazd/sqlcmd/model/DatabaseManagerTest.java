package ua.com.juja.garazd.sqlcmd.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
import static org.junit.Assert.fail;

public class DatabaseManagerTest {

    private static ConfigurationTest configurationTest = new ConfigurationTest();
    private static String DATABASE_NAME = configurationTest.getDatabaseNameForTest();
    private static String USER_NAME = configurationTest.getUserNameForTest();
    private static String PASSWORD = configurationTest.getPasswordForTest();
    private static String TEST_TABLE_NAME = "users";
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

    @Test(expected = DatabaseManagerException.class)
    public void testConnectToDatabaseWhenIncorrectUserAndPassword() {
        //given
        //when
        try {
            manager.connectDatabase(DATABASE_NAME, "notExistUser", "qwerty");
            fail();
        } catch (Exception e) {
            //then
            manager.connectDatabase(DATABASE_NAME, USER_NAME, PASSWORD);
            throw e;
        }
    }

    @Test
    public void testGetAllTableNames() {
        // given
        Set<String> expected = new LinkedHashSet<>();
        expected.add(TEST_TABLE_NAME);

        // when
        Set<String> tableNames = manager.getTableNames();

        // then
        assertEquals(expected, tableNames.toString());
    }

    @Test
    public void testGetTableData() {
        //given
        manager.clearTable(TEST_TABLE_NAME);

        //when
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("name", "Stiven");
        input.put("password", "pass");
        input.put("id", 4);
        manager.createEntry(TEST_TABLE_NAME, input);

        //then
        List<Map<String, Object>> users = new ArrayList<>();
        assertEquals(1, users.size());

        Map<String, Object> user = users.get(0);
        assertEquals("[name, password, id]", user.keySet().toString());
        assertEquals("[Stiven, pass, 4]", user.keySet().toString());
    }

    @Test
    public void testUpdateTableData() {
        //given
        manager.clearTable(TEST_TABLE_NAME);

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("name", "Stiven");
        input.put("password", "pass");
        input.put("id", 4);
        manager.createEntry(TEST_TABLE_NAME, input);

        //when
        Map<String, Object> newValue = new LinkedHashMap<>();
        newValue.put("password", "pass2");
        newValue.put("name", "Eva");
        manager.updateTable(TEST_TABLE_NAME, 4, newValue);

        //then
        List<Map<String, Object>> users = manager.getTableData(TEST_TABLE_NAME);
        assertEquals(1, users.size());

        Map<String, Object> user = users.get(0);
        assertEquals("[name, password, id]", user.keySet().toString());
        assertEquals("[Eva, pass2, 4]", user.keySet().toString());
    }

    @Test
    public void testGetColumnNames() {
        //given
        manager.clearTable(TEST_TABLE_NAME);

        //when
        Set<String> columnNames = manager.getTableColumns(TEST_TABLE_NAME);

        //then
        assertEquals("[id, username, password]", columnNames.toString());
    }

    @Test
    public void testIsConnected() {
        //given
        //when
        //then
        assertTrue(manager.isConnected());
    }
}