package ua.com.juja.garazd.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UpdateEntryTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new UpdateEntry(manager, view);
    }

    @Test
    public void testCanProcessUpdateEntryParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("updateEntry|user");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessUpdateEntryWithoutParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("updateEntry");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessQweString() {
        // given

        // when
        boolean canProcess = command.canProcess("update|user");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsLessThenTwo() {
        // when
        String expected = "1";
        try {
            command.process("updateEntry");
            fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // then
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersMoreThenTwo() {

        // when
        try {
            command.process("updateEntry|user|qwe");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("For input string: \"qwe\"", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorMassageWithUpdateEntry() {
        // given

        // when
        try {
            command.process("updateEntry|user");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Must be an odd number of parameters in a format 'updateEntry|tableName|idColumn|columnName1|newValue1|" +
                "columnName2|newValue2|...|columnNameN|newValueN', but you sent: 'updateEntry|user'", e.getMessage());
        }
    }

    @Test
    public void testProcessUpdateEntry() {
        // given

        // when
        command.process("createEntry|user|5|name|Vasya|pass|qwerty");

        // then
        verify(view).write("Recording 5 in the table 'user' successfully updated.");
    }
}