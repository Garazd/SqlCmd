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

public class DropDatabaseTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new DropDatabase(manager, view);
    }

    @Test
    public void testDropDatabase() {
        // given

        // when
        command.process("dropDatabase|user");

        // then
        verify(manager).dropDatabase("user");
        verify(view).write("Base 'user' dropped.");
    }

    @Test
    public void testCanProcessDropDatabaseParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("dropDatabase|user");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessDropDatabaseWithoutParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("dropDatabase");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessQweString() {
        // given

        // when
        boolean canProcess = command.canProcess("Drop|user");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsLessThenTwo() {
        // when
        try {
            command.process("dropDatabase");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Command format is 'dropDatabase|databaseName', and you input: dropDatabase", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersMoreThenTwo() {

        // when
        try {
            command.process("dropDatabase|user|qwerty");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Command format is 'dropDatabase|databaseName', and you input: dropDatabase|user|qwerty", e.getMessage());
        }
    }

    @Test
    public void testProcessCommandDropDatabase() {
        // given

        // when
        command.process("dropDatabase|user");

        // then
        verify(view).write("Base 'user' dropped.");
    }
}