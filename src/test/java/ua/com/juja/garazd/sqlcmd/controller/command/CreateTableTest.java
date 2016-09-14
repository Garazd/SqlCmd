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

public class CreateTableTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new CreateTable(manager, view);
    }

    @Test
    public void testCreateTable() {
        // given

        // when
        command.process("createTable|user");

        // then
        verify(manager).createTable("user");
        verify(view).write("Table 'user' created.");
    }

    @Test
    public void testCanProcessCreateTableParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("createTable|user");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessCreateTableWithoutParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("createTable");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessQweString() {
        // given

        // when
        boolean canProcess = command.canProcess("Table|user");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsLessThenTwo() {
        // when
        try {
            command.process("createTable");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Command format is 'createTable|tableName', and you input: createTable", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersMoreThenTwo() {

        // when
        try {
            command.process("createTable|user|qwe");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Command format is 'createTable|tableName', and you input: createTable|user|qwe", e.getMessage());
        }
    }
}