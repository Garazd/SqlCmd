package ua.com.juja.garazd.sqlcmd.integration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.garazd.sqlcmd.Main;
import ua.com.juja.garazd.sqlcmd.controller.properties.ConfigurationTest;
import ua.com.juja.garazd.sqlcmd.controller.properties.Support;
import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.model.DatabaseManagerImpl;
import static org.junit.Assert.assertEquals;

public class IntegrationTest {

    private static ConfigurationTest configurationTest = new ConfigurationTest();
    private static String DATABASE_NAME = configurationTest.getDatabaseNameForTest();
    private static String USER_NAME = configurationTest.getUserNameForTest();
    private static String PASSWORD = configurationTest.getPasswordForTest();
    private static Support support;
    private static DatabaseManager manager;
    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;
    private String commandConnect = "connect|" + DATABASE_NAME + "|" + USER_NAME + "|" + PASSWORD;
    private String welcomeSQLCmd =
        "=================================================================\r\n" +
        "======================= Welcome to SQLCmd =======================\r\n" +
        "=================================================================\r\n" +
        "                                                                 \r\n" +
        "Please specify the connection settings in the configuration file\r\n" +
        "and enter the command 'connect' to work with the database\r\n";

    @BeforeClass
    public static void buildDatabase() {
        manager = new DatabaseManagerImpl();
        support = new Support();
        support.setupData(manager);
    }

    @Before
    public void setup() {
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @AfterClass
    public static void dropDatabase() {
        support.dropData(manager);
    }
    
    @Test
    public void testClear() {
        // given
        in.add(commandConnect);
        in.add("clearTable|user");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // clearTable|user
            "Table user has been successfully cleared.\r\n" +
            // exit
            "See you later! Bye\r\n", getData());
    }

    @Test
    public void testClearWithError() {
        // given
        in.add(commandConnect);
        in.add("clearTable|sadfasd|fsf|fdsf");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(welcomeSQLCmd +
            "Success!\r\n" +
            "Enter a command (or help for assistance):\r\n" +
            // clearTable|sadfasd|fsf|fdsf
            "Failure! because of: Command format is 'clearTable|tableName', and you input: clearTable|sadfasd|fsf|fdsf\r\n" +
            "Please try again.\r\n" +
            "Enter a command (or help for assistance):\r\n" +
            // exit
            "See you later! Bye\r\n", getData());
    }

    @Test
    public void testClearAndCreateTableData() {
        // given
        in.add(commandConnect);
        in.add("clearTable|user");
        in.add("createEntry|user|id|13|name|Stiven|password|*****");
        in.add("createEntry|user|id|14|name|Eva|password|+++++");
        in.add("find|user");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // clearTable|user
            "Table user has been successfully cleared.\r\n" +
            // createEntry|user|id|13|name|Stiven|password|*****
            "Recording {names:[id, name, password], values:[13, Stiven, *****]} was successfully created in the table 'user'.\r\n" +
            // createEntry|user|id|14|name|Eva|password|+++++
            "Recording {names:[id, name, password], values:[14, Eva, +++++]} was successfully created in the table 'user'.\r\n" +
            // find|user
            "--------------------\r\n" +
            "|name|password|id|\r\n" +
            "--------------------\r\n" +
            "|Stiven|*****|13|\r\n" +
            "|Eva|+++++|14|\r\n" +
            "--------------------\r\n" +
            // exit
            "See you later! Bye\r\n", getData());
    }

    @Test
    public void testCreateWithErrors() {
        // given
        in.add(commandConnect);
        in.add("createEntry|user|error");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // createEntry|user|error
            "Failure! because of: Must be an even number of parameters in a format 'createEntry|tableName|column1|value1|column2|value2|...|columnN|valueN', but you sent: 'createEntry|user|error'\r\n" +
            "Please try again.\r\n" +
            // exit
            "See you later! Bye\r\n", getData());
    }

    @Test
    public void testExit() {
        // given
        in.add(commandConnect);
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // exit
            "See you later! Bye\r\n", getData());
    }

    @Test
    public void testFind() {
        // given
        in.add(commandConnect);
        in.add("find|user");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // find|user
            "--------------------\r\n" +
            "|name|password|id|\r\n" +
            "--------------------\r\n" +
            "--------------------\r\n" +
            // exit
            "See you later! Bye\r\n", getData());
    }

    @Test
    public void testHelp() {
        // given
        in.add(commandConnect);
        in.add("help");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // help
            "Existing command:\r\n" +
            "\tlist\r\n" +
            "\t\tfor a list of all database tables, is connected to\r\n" +
            "\tclearTable|tableName\r\n" +
            "\t\tto clean up the entire table\r\n" +
            "\tcreateEntry|tableName|column1|value1|column2|value2|...|columnN|valueN\r\n" +
            "\t\tto createEntry a record in the table\r\n" +
            "\tfind|tableName\r\n" +
            "\t\tto get the contents of the table 'tableName'\r\n" +
            "\thelp\r\n" +
            "\t\tto display the list on the screen\r\n" +
            "\texit\r\n" +
            "\t\tto exit from the program\r\n" +
            // exit
            "See you later! Bye\r\n", getData());
    }

    @Test
    public void testTables() {
        // given
        in.add(commandConnect);
        in.add("list");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // list
            "[user, test]\r\n" +
            // exit
            "See you later! Bye\r\n", getData());
    }

    @Test
    public void testUnsupported() {
        // given
        in.add(commandConnect);
        in.add("unsupported");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // unsupported
            "Nonexistent command: unsupported\r\n" +
            "Enter command (or help for help):\r\n" +
            // exit
            "See you later! Bye\r\n", getData());
    }

    @Test
    public void testNonexistentCommand() {
        // given
        in.add(commandConnect);
        in.add("list");
        in.add("connectDatabase");
        in.add("list");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // list
            "[user, test]\r\n" +
            // connectDatabase
            "Nonexistent command: connectDatabase\r\n" +
            "Enter command (or help for help):\r\n" +
            // list
            "[user, test]\r\n" +
            // exit
            "See you later! Bye\r\n", getData());
    }

    public String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
}