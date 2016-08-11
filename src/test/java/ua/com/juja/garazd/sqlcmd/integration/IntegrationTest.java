package ua.com.juja.garazd.sqlcmd.integration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.garazd.sqlcmd.Main;
import ua.com.juja.garazd.sqlcmd.controller.properties.Configuration;
import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.model.JDBCDatabaseManager;
import static org.junit.Assert.assertEquals;

public class IntegrationTest {

    private static Configuration configuration = new Configuration();
    private static String DATABASE_NAME = configuration.getDatabaseName();
    private static String USER_NAME = configuration.getUserName();
    private static String PASSWORD = configuration.getPassword();

    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    private static DatabaseManager manager;

    @BeforeClass
    public static void init() {
        manager = new JDBCDatabaseManager();
        manager.connect(DATABASE_NAME, USER_NAME, PASSWORD);
    }
    @Before
    public void setup() {
        manager.connect(DATABASE_NAME, USER_NAME, PASSWORD);
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
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

    @Test
    public void testClear() {
        // given
        in.add("clear|user");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // clear|user
            "Table user has been successfully cleared.\r\n" +
            // exit
            "See you later! Bye\r\n", getData());
    }

    @Test
    public void testClearWithError() {
        // given
        in.add("clear|sadfasd|fsf|fdsf");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // clear|sadfasd|fsf|fdsf
            "Failure! because of: command format is 'clear|tableName', and you have brought: clear|sadfasd|fsf|fdsf\r\n" +
            "Please try again.\r\n" +
            // exit
            "See you later! Bye\r\n", getData());
    }

    @Test
    public void testClearAndCreateTableData() {
        // given
        in.add("clear|user");
        in.add("create|user|id|13|name|Stiven|password|*****");
        in.add("create|user|id|14|name|Eva|password|+++++");
        in.add("find|user");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // clear|user
            "Table user has been successfully cleared.\r\n" +
            // create|user|id|13|name|Stiven|password|*****
            "Recording {names:[id, name, password], values:[13, Stiven, *****]} was successfully created in the table 'user'.\r\n" +
            // create|user|id|14|name|Eva|password|+++++
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
        in.add("create|user|error");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // create|user|error
            "Failure! because of: Must be an even number of parameters in a format 'create|tableName|column1|value1|column2|value2|...|columnN|valueN', but you sent: 'create|user|error'\r\n" +
            "Please try again.\r\n" +
            // exit
            "See you later! Bye\r\n", getData());
    }

    @Test
    public void testExit() {
        // given
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
            "\tclear|tableName\r\n" +
            "\t\tto clean up the entire table\r\n" +
            "\tcreate|tableName|column1|value1|column2|value2|...|columnN|valueN\r\n" +
            "\t\tto create a record in the table\r\n" +
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
        in.add("list");
        in.add("connect");
        in.add("list");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // list
            "[user, test]\r\n" +
            // connect
            "Nonexistent command: connect\r\n" +
            "Enter command (or help for help):\r\n" +
            // list
            "[user, test]\r\n" +
            // exit
            "See you later! Bye\r\n", getData());
    }
}