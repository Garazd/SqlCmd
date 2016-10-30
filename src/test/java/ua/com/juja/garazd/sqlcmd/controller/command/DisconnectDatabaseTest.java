package ua.com.juja.garazd.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DisconnectDatabaseTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new DisconnectDatabase(manager, view);
    }

    @Test
    public void testCanProcess() {
        // given

        // when
        boolean canProcess = command.canProcess("disconnect");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testWithConfirmDropDB() {
        // given

        //when
        command.process("disconnect");

        //then
        verify(manager).disconnectDatabase();
        verify(view).write("Disconnected");
    }
}