package ua.com.juja.garazd.sqlcmd.model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.garazd.sqlcmd.controller.properties.Configuration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseManagerTest {

    private static Configuration configuration = new Configuration();
    private static String DATABASE_NAME = configuration.getDatabaseNameForTest();
    private static String USER_NAME = configuration.getUserNameForTest();
    private static String PASSWORD = configuration.getPasswordForTest();
    private static String TABLE_NAME_FIRST = "test_table";
    private static String CREATE_TABLE = TABLE_NAME_FIRST + " (id SERIAL PRIMARY KEY," +
        " username VARCHAR (50) UNIQUE NOT NULL," +
        " password VARCHAR (50) NOT NULL)";
    private static DatabaseManager manager;
    private static Connection connection;

    @BeforeClass
    public static void init() {
        manager = new DatabaseManagerImpl();
        manager.connectDatabase("", USER_NAME, PASSWORD);
        manager.dropDatabase(DATABASE_NAME);
        manager.createDatabase(DATABASE_NAME);
    }

    @AfterClass
    public static void clearAfterAllTests() {
        manager.connectDatabase(DATABASE_NAME, USER_NAME, PASSWORD);
        manager.dropDatabase(DATABASE_NAME);
    }

    @Before
    public void setup() {
        manager.connectDatabase(DATABASE_NAME, USER_NAME, PASSWORD);
        manager.createTable(CREATE_TABLE);
    }

    @After
    public void clear() {
        manager.dropTable(TABLE_NAME_FIRST);
    }

    @Test
    public void testGetAllTableNames() {
        // given
        Set<String> expected = new LinkedHashSet<>();
        expected.add(TABLE_NAME_FIRST);

        // when
        Set<String> tableNames = manager.getTableNames();

        // then
        assertEquals(expected, tableNames.toString());
    }

    @Test
    public void testGetTableData() {
        //given
        manager.clearTable(TABLE_NAME_FIRST);

        //when
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("name", "Stiven");
        input.put("password", "pass");
        input.put("id", 4);
        manager.createEntry(TABLE_NAME_FIRST, input);

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
        manager.clearTable(TABLE_NAME_FIRST);

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("name", "Stiven");
        input.put("password", "pass");
        input.put("id", 4);
        manager.createEntry("user", input);

        //when
        Map<String, Object> newValue = new LinkedHashMap<>();
        newValue.put("password", "pass2");
        newValue.put("name", "Eva");
        manager.updateTable("user", 4, newValue);

        //then
        List<Map<String, Object>> users = manager.getTableData(TABLE_NAME_FIRST);
        assertEquals(1, users.size());

        Map<String, Object> user = users.get(0);
        assertEquals("[name, password, id]", user.keySet().toString());
        assertEquals("[Eva, pass2, 4]", user.keySet().toString());
    }

    @Test
    public void testGetColumnNames() {
        //given
        manager.clearTable(TABLE_NAME_FIRST);

        //when
        Set<String> columnNames = manager.getTableColumns(TABLE_NAME_FIRST);

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