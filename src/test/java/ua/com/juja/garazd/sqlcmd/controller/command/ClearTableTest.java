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
    public void testClearTable() {
        // given

        // when
        command.process("clearTable|user");

        // then
        verify(manager).clearTable("user");
        verify(view).write("Table user has been successfully cleared.");
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
    public void testCanProcessQweString() {
        // given

        // when
        boolean canProcess = command.canProcess("qwe|user");

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
            assertEquals("command format is 'clearTable|tableName', and you have brought: clearTable", e.getMessage());
        }
    }
    
    @Test
    public void testValidationErrorWhenCountParametersMoreThenTwo() {

        // when
        try {
            command.process("clearTable|table|qwe");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("command format is 'clearTable|tableName', and you have brought: clearTable|table|qwe", e.getMessage());
        }
    }
}