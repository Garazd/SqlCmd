package ua.com.juja.garazd.sqlcmd.controller.command;

import org.junit.Test;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ExitTest {

    private FakeView view = new FakeView();

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
        assertEquals("See you later!\n", view.getContent());
        //throws
    }
}