package ua.com.juja.garazd.sqlcmd.controller.command;

import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.garazd.sqlcmd.view.View;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ExitWithMockitoTest {

    private View view = Mockito.mock(View.class);

    @Test
    public void testCanProcessExitString() {
        //given
        Command command = new Exit(view);

        //when
        boolean canProcess = command.canProcess("exit");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessQweString() {
        //given
        Command command = new Exit(view);

        //when
        boolean canProcess = command.canProcess("qwe");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testProcessExitCommandThrowsExitException() {
        //given
        Command command = new Exit(view);

        //when
        try {
            command.process("qwe");
            fail("Expected ExitException");
        } catch (ExitException e) {
            //do nothing;
        }

        //then
        Mockito.verify(view).write("See you later! Bye");
    }
}