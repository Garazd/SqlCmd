package ua.com.juja.garazd.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HelpTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Help(view);
    }

    @Test
    public void testCanProcessHelpWithParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("help");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessHelpWithoutParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessQweString() {
        // given

        // when
        boolean canProcess = command.canProcess("help|user");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testViewOutput() {
        // given

        // when
        command.process("help");

        // then
        verify(view).write("Existing command:");
    }
}