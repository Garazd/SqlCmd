package ua.com.juja.garazd.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.garazd.sqlcmd.view.View;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ExitWithMockitoTest {

    private View view;
    private Command command;

    @Before
    public void setup() {
        view = mock(View.class);
        command = new Exit(view);
    }

    @Test
    public void testCanProcessExitString() {
        //given

        //when
        boolean canProcess = command.canProcess("exit");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessQweString() {
        //given

        //when
        boolean canProcess = command.canProcess("qwe");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testProcessExitCommandThrowsExitException() {
        //given

        //when
        try {
            command.process("qwe");
            fail("Expected ExitException");
        } catch (ExitException e) {
            //do nothing;
        }

        //then
        verify(view).write("See you later! Bye");
    }
}