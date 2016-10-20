package ua.com.juja.garazd.sqlcmd.controller.command;

import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.garazd.sqlcmd.view.View;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClearTableTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new ClearTable(manager, view);
    }

    @Test
    public void testCanProcessClearWithParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("clearTable|user");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessClearWithoutParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("clearTable");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessSomeString() {
        // given

        // when
        boolean canProcess = command.canProcess("some");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsLessThenTwo() {
        // when
        try {
            command.process("clearTable");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Command format is 'clearTable|tableName', and you input: clearTable", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersMoreThenTwo() {

        // when
        try {
            command.process("clearTable|table|some");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Command format is 'clearTable|tableName', and you input: clearTable|table|some", e.getMessage());
        }
    }

    @Test
    public void testProcessCommandClearTable() {
        // given

        // when
        command.process("clearTable|user");

        // then
        verify(view).write("Delete the data from the table 'user'. Y/N");
    }

    @Test
    public void testWithConfirmClear() {
        // given

        //when
        when(view.read()).thenReturn("y");
        command.process("clearTable|user");
        //then
        verify(manager).clearTable("user");
        verify(view).write("Table user has been successfully cleared.");
    }

    @Test
    public void testWithNotConfirmClear() {
        // given

        // when
        when(view.read()).thenReturn("n");
        command.process("clearTable|user");

        // then
        verify(view).write("Table data will not removed");
    }
}