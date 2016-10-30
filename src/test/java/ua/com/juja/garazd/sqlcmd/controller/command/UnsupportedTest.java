package ua.com.juja.garazd.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UnsupportedTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Unsupported(view);
    }

    @Test
    public void testCanProcessUnsupportedWithParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("unsupported");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessUnsupportedWithoutParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessQweString() {
        // given

        // when
        boolean canProcess = command.canProcess("unsupported|user");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testProcessUnsupportedCommand() {
        // given

        // when
        command.process("wrongCommand");

        // then
        verify(view).write("Command: wrongCommand does not exist");
    }
}