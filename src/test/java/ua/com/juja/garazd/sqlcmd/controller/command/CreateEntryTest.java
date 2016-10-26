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

public class CreateEntryTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new CreateEntry(manager, view);
    }

    @Test
    public void testCanProcessCreateEntryParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("createEntry|user");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessCreateEntryWithoutParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("createEntry");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessQweString() {
        // given

        // when
        boolean canProcess = command.canProcess("Entry|user");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsLessThenTwo() {
        // when
        try {
            command.process("createEntry");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Must be an even number of parameters in a format 'createEntry|tableName|column1|value1|column2|value2|...|columnN|valueN', but you sent: 'createEntry'", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersMoreThenTwo() {

        // when
        try {
            command.process("createEntry|user|qwe");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Must be an even number of parameters in a format 'createEntry|tableName|column1|value1|column2|value2|...|columnN|valueN', but you sent: 'createEntry|user|qwe'", e.getMessage());
        }
    }

    @Test
    public void testProcessCreateEntry() {
        // given

        // when
        command.process("createEntry|user");

        // then
        verify(view).write("Recording {} was successfully created in the table 'user'.");
    }
}