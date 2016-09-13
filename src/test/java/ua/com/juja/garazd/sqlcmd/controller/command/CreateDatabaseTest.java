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

public class CreateDatabaseTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new CreateDatabase(manager, view);
    }

    @Test
    public void testCreateDatabase() {
        // given

        // when
        command.process("createDatabase|user");

        // then
        verify(manager).createDatabase("user");
        verify(view).write("Base 'user' created.");
    }


    @Test
    public void testCanProcessCreateDatabaseParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("createDatabase|user");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessCreateDatabaseWithoutParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("createDatabase");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessQweString() {
        // given

        // when
        boolean canProcess = command.canProcess("Database|user");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsLessThenTwo() {
        // when
        try {
            command.process("createDatabase");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Command format is 'createDatabase|databaseName', and you input: createDatabase", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersMoreThenTwo() {

        // when
        try {
            command.process("createDatabase|user|qwe");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Command format is 'createDatabase|databaseName', and you input: createDatabase|user|qwe", e.getMessage());
        }
    }
}
