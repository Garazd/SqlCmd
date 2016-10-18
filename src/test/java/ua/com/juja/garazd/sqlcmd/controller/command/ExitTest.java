package ua.com.juja.garazd.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class ExitTest {

    private FakeView view;
    private Command command;

    @Before
    public void setup() {
        view = new FakeView();
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
        assertEquals("See you later! Bye\n", view.getContent());
        //throws
    }
}