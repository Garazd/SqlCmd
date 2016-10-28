package ua.com.juja.garazd.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConnectDatabaseTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new ConnectDatabase(manager, view);
    }

    @Test
    public void testCanProcessWithParameters() {
        // when
        boolean canProcess = command.canProcess("connect|sqlcmd|postgres|postgres");
        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCorrectConnectCommand() {
        //when
        command.process("connect|sqlcmd|postgres|postgres");

        //then
        verify(view).write("Success!");
    }

    @Test
    public void testValidationError() {
        // when
        try {
            command.process("connect|sqlcmd|postgres");

        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Invalid number of parameters separated by " +
                "sign '|', expected connect|databaseName|userName|password, and you input: connect|sqlcmd|postgres", e.getMessage());
        }
    }
}