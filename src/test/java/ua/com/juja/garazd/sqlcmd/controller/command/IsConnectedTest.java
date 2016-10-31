package ua.com.juja.garazd.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class IsConnectedTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new IsConnected(manager, view);
    }

    @Test
    public void testProcess() {
        command.process("not connected");
        verify(view).write("You can not use the command 'not connected' " +
            "is until you join using command 'connect|databaseName|userName|password'");
    }

    @Test
    public void testCanProcessWithParameters() {
        // when
        boolean canProcess = command.canProcess("connect");
        // then
        assertTrue(canProcess);
    }
}