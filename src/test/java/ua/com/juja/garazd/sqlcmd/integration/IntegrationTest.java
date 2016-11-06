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
import ua.com.juja.garazd.sqlcmd.model.PostgresDatabaseManager;
import static org.junit.Assert.assertEquals;

public class IntegrationTest {

    private static ConfigurationTest configurationTest = new ConfigurationTest();
    private static String DATABASE_NAME = configurationTest.getDatabaseNameForTest();
    private static String USER_NAME = configurationTest.getUserNameForTest();
    private static String PASSWORD = configurationTest.getPasswordForTest();
    private static String TEST_TABLE = "test";
    private static String USERS_TABLE = "users";
    private static Support support;
    private static DatabaseManager manager;
    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;
    private String commandConnect = "connect|" + DATABASE_NAME + "|" + USER_NAME + "|" + PASSWORD;
    private String welcomeSQLCmd =
        "=================================================================\n" +
        "======================= Welcome to SQLCmd =======================\n" +
        "=================================================================\n" +
        "                                                                 \n" +
        "Please specify the connection settings in the configuration file\n" +
        "and enter the command 'connect|databaseName|userName|password' to work with the database\n";

    @BeforeClass
    public static void buildDatabase() {
        manager = new PostgresDatabaseManager();
        support = new Support();
        support.setupData(manager);
        manager.createTable(TEST_TABLE +
            " (id SERIAL NOT NULL PRIMARY KEY, name VARCHAR (50) UNIQUE NOT NULL, password VARCHAR (50) NOT NULL)");
        manager.createTable(USERS_TABLE +
            " (id SERIAL NOT NULL PRIMARY KEY, name VARCHAR (50) UNIQUE NOT NULL, password VARCHAR (50) NOT NULL)");
    }

    @AfterClass
    public static void dropDatabase() {
        support.dropData(manager);
    }

    @Before
    public void setup() {
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testClear() {
        // given
        in.add(commandConnect);
        in.add("clearTable|users");
        in.add("Y");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(welcomeSQLCmd +
            "Success!\n" +
            "Enter a command (or help for assistance):\n" +
            // clearTable|users
            "Delete the data from the table 'users'. Y/N\n" +
            "Table users has been successfully cleared.\n" +
            "Enter a command (or help for assistance):\n" +
            // exit
            "See you later! Bye\n", getData());
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
            "Success!\n" +
            "Enter a command (or help for assistance):\n" +
            // clearTable|sadfasd|fsf|fdsf
            "Failure! because of: Command format is 'clearTable|tableName', and you input: clearTable|sadfasd|fsf|fdsf\n" +
            "Please try again.\n" +
            "Enter a command (or help for assistance):\n" +
            // exit
            "See you later! Bye\n", getData());
    }

    @Test
    public void testClearAndCreateTableData() {
        // given
        in.add(commandConnect);
        in.add("clearTable|users");
        in.add("y");
        in.add("createEntry|users|id|13|name|Stiven|password|*****");
        in.add("createEntry|users|id|14|name|Eva|password|+++++");
        in.add("contents|users");
        in.add("clearTable|users");
        in.add("y");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(welcomeSQLCmd +
            "Success!\n" +
            "Enter a command (or help for assistance):\n" +
            // clearTable|users
            "Delete the data from the table 'users'. Y/N\n" +
            "Table users has been successfully cleared.\n" +
            "Enter a command (or help for assistance):\n" +
            // createEntry|users|id|13|name|Stiven|password|*****
            "Recording {id=13, name=Stiven, password=*****} was successfully created in the table 'users'.\n" +
            "Enter a command (or help for assistance):\n" +
            // createEntry|users|id|14|name|Eva|password|+++++
            "Recording {id=14, name=Eva, password=+++++} was successfully created in the table 'users'.\n" +
            "Enter a command (or help for assistance):\n" +
            // contents|users
            "+--+------+--------+\n" +
            "|id|name  |password|\n" +
            "+--+------+--------+\n" +
            "|13|Stiven|*****   |\n" +
            "+--+------+--------+\n" +
            "|14|Eva   |+++++   |\n" +
            "+--+------+--------+\n" +
            "Enter a command (or help for assistance):\n" +
            // clearTable|users
            "Delete the data from the table 'users'. Y/N\n" +
            "Table users has been successfully cleared.\n" +
            "Enter a command (or help for assistance):\n" +
            // exit
            "See you later! Bye\n", getData());
    }

    @Test
    public void testCreateWithErrors() {
        // given
        in.add(commandConnect);
        in.add("createEntry|users|error");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(welcomeSQLCmd +
            "Success!\n" +
            "Enter a command (or help for assistance):\n" +
            // createEntry|users|error
            "Failure! because of: Must be an even number of parameters in a format 'createEntry|tableName|column1|value1|column2|value2|...|columnN|valueN', but you sent: 'createEntry|users|error'\n" +
            "Please try again.\n" +
            "Enter a command (or help for assistance):\n" +
            // exit
            "See you later! Bye\n", getData());
    }

    @Test
    public void testExit() {
        // given
        in.add(commandConnect);
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(welcomeSQLCmd +
            "Success!\n" +
            "Enter a command (or help for assistance):\n" +
            // exit
            "See you later! Bye\n", getData());
    }

    @Test
    public void testGetTableData() {
        // given
        in.add(commandConnect);
        in.add("contents|users");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(welcomeSQLCmd +
            "Success!\n" +
            "Enter a command (or help for assistance):\n" +
            // contents|users
            "+--+----+--------+\n" +
            "|id|name|password|\n" +
            "+--+----+--------+\n" +
            "Enter a command (or help for assistance):\n" +
            // exit
            "See you later! Bye\n", getData());
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
        assertEquals(welcomeSQLCmd +
            "Success!\n" +
            "Enter a command (or help for assistance):\n" +
            // help
            "Existing command:\n" +
            "\thelp\n" +
            "\t\tto show all commands\n" +
            "\tdatabases\n" +
            "\t\tto to show all databases\n" +
            "\tcreateDatabase|databaseName\n" +
            "\t\tto create the database\n" +
            "\tdropDatabase|databaseName\n" +
            "\t\tto drop the database\n" +
            "\tcreateTable|tableName(columnName1 type, columnName2 type,...columnNameN type)\n" +
            "\t\tto create the table with the name. Type can be text or integer\n" +
            "\tdropTable|tableName\n" +
            "\t\tto drop the table\n" +
            "\tcreateEntry|tableName|column1|value1|column2|value2|...|columnN|valueN\n" +
            "\t\tto create entries the table\n" +
            "\tclearTable|tableName\n" +
            "\t\tto clear the table\n" +
            "\tshow\n" +
            "\t\tto show all tables in the database\n" +
            "\tcontents|tableName\n" +
            "\t\tto get the contents of the table\n" +
            "\texit\n" +
            "\t\tto exit the program\n" +
            "Enter a command (or help for assistance):\n" +
            "See you later! Bye\n", getData());
    }

    @Test
    public void testTables() {
        // given
        in.add(commandConnect);
        in.add("show");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(welcomeSQLCmd +
            "Success!\n" +
            "Enter a command (or help for assistance):\n" +
            // show
            "[test, users, users2]\n" +
            "Enter a command (or help for assistance):\n" +
            // exit
            "See you later! Bye\n", getData());
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
        assertEquals(welcomeSQLCmd +
            "Success!\n" +
            "Enter a command (or help for assistance):\n" +
            // unsupported
            "Command: unsupported does not exist\n" +
            "Enter a command (or help for assistance):\n" +
            // exit
            "See you later! Bye\n", getData());
    }

    @Test
    public void testNonexistentCommand() {
        // given
        in.add(commandConnect);
        in.add("show");
        in.add("connectDatabase");
        in.add("show");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(welcomeSQLCmd +
            "Success!\n" +
            "Enter a command (or help for assistance):\n" +
            // show
            "[test, users, users2]\n" +
            "Enter a command (or help for assistance):\n" +
            // connectDatabase
            "Failure! because of: Invalid number of parameters separated by sign '|', " +
            "expected connect|databaseName|userName|password, and you input: connectDatabase\n" +
            "Please try again.\n" +
            "Enter a command (or help for assistance):\n" +
            // show
            "[test, users, users2]\n" +
            "Enter a command (or help for assistance):\n" +
            // exit
            "See you later! Bye\n", getData());
    }

    public String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8").replaceAll("\r\n", "\n");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
}