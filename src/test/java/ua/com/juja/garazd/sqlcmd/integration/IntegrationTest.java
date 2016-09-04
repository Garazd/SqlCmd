package ua.com.juja.garazd.sqlcmd.integration;

import org.junit.Test;
import ua.com.juja.garazd.sqlcmd.Main;

public class IntegrationTest {

    private ConsoleMock console = new ConsoleMock();

    @Test
    public void testClear() {
        // given
        console.addIn("clearTable|user");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertOut("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // clearTable|user
            "Table user has been successfully cleared.\r\n" +
            // exit
            "See you later! Bye\r\n");
    }

    @Test
    public void testClearWithError() {
        // given
        console.addIn("clearTable|sadfasd|fsf|fdsf");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertOut("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // clearTable|sadfasd|fsf|fdsf
            "Failure! because of: command format is 'clearTable|tableName', and you have brought: clearTable|sadfasd|fsf|fdsf\r\n" +
            "Please try again.\r\n" +
            // exit
            "See you later! Bye\r\n");
    }

    @Test
    public void testClearAndCreateTableData() {
        // given
        console.addIn("clearTable|user");
        console.addIn("createTable|user|id|13|name|Stiven|password|*****");
        console.addIn("createTable|user|id|14|name|Eva|password|+++++");
        console.addIn("find|user");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertOut("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // clearTable|user
            "Table user has been successfully cleared.\r\n" +
            // createTable|user|id|13|name|Stiven|password|*****
            "Recording {names:[id, name, password], values:[13, Stiven, *****]} was successfully created in the table 'user'.\r\n" +
            // createTable|user|id|14|name|Eva|password|+++++
            "Recording {names:[id, name, password], values:[14, Eva, +++++]} was successfully created in the table 'user'.\r\n" +
            // find|user
            "--------------------\r\n" +
            "|name|password|id|\r\n" +
            "--------------------\r\n" +
            "|Stiven|*****|13|\r\n" +
            "|Eva|+++++|14|\r\n" +
            "--------------------\r\n" +
            // exit
            "See you later! Bye\r\n");
    }

    @Test
    public void testCreateWithErrors() {
        // given
        console.addIn("createTable|user|error");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertOut("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // createTable|user|error
            "Failure! because of: Must be an even number of parameters in a format 'createTable|tableName|column1|value1|column2|value2|...|columnN|valueN', but you sent: 'createTable|user|error'\r\n" +
            "Please try again.\r\n" +
            // exit
            "See you later! Bye\r\n");
    }

    @Test
    public void testExit() {
        // given
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertOut("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // exit
            "See you later! Bye\r\n");
    }

    @Test
    public void testFind() {
        // given
        console.addIn("find|user");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertOut("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // find|user
            "--------------------\r\n" +
            "|name|password|id|\r\n" +
            "--------------------\r\n" +
            "--------------------\r\n" +
            // exit
            "See you later! Bye\r\n");
    }

    @Test
    public void testHelp() {
        // given
        console.addIn("help");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertOut("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // help
            "Existing command:\r\n" +
            "\tlist\r\n" +
            "\t\tfor a list of all database tables, is connected to\r\n" +
            "\tclearTable|tableName\r\n" +
            "\t\tto clean up the entire table\r\n" +
            "\tcreateTable|tableName|column1|value1|column2|value2|...|columnN|valueN\r\n" +
            "\t\tto createTable a record in the table\r\n" +
            "\tfind|tableName\r\n" +
            "\t\tto get the contents of the table 'tableName'\r\n" +
            "\thelp\r\n" +
            "\t\tto display the list on the screen\r\n" +
            "\texit\r\n" +
            "\t\tto exit from the program\r\n" +
            // exit
            "See you later! Bye\r\n");
    }

    @Test
    public void testTables() {
        // given
        console.addIn("list");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertOut("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // list
            "[user, test]\r\n" +
            // exit
            "See you later! Bye\r\n");
    }

    @Test
    public void testUnsupported() {
        // given
        console.addIn("unsupported");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertOut("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // unsupported
            "Nonexistent command: unsupported\r\n" +
            "Enter command (or help for help):\r\n" +
            // exit
            "See you later! Bye\r\n");
    }

    @Test
    public void testNonexistentCommand() {
        // given
        console.addIn("list");
        console.addIn("connectDatabase");
        console.addIn("list");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertOut("Connecting to a database is successful\r\n" +
            "Enter command (or help for help):\r\n" +
            // list
            "[user, test]\r\n" +
            // connectDatabase
            "Nonexistent command: connectDatabase\r\n" +
            "Enter command (or help for help):\r\n" +
            // list
            "[user, test]\r\n" +
            // exit
            "See you later! Bye\r\n");
    }

    private void assertOut(String expected, String... parameters) {
        String string = expected.replaceAll("\\n", "\r\n");
        if (parameters.length > 0) {
            string = string.replaceAll("%s", parameters[0]);
        }
        assertOut(string, console.getOut());
    }
}