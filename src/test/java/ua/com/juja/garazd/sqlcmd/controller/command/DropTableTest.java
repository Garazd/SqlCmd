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

public class DropTableTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new DropTable(manager, view);
    }

    @Test
    public void testDropTable() {
        // given

        // when
        command.process("dropTable|user");

        // then
        verify(manager).dropTable("user");
        verify(view).write("Table 'user' dropped.");
    }

    @Test
    public void testCanProcessDropTableParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("dropTable|user");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessDropTableWithoutParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("dropTable");

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
            command.process("dropTable");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Command format is 'dropTable|tableName', and you input: dropTable", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersMoreThenTwo() {

        // when
        try {
            command.process("dropTable|user|qwe");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Command format is 'dropTable|tableName', and you input: dropTable|user|qwe", e.getMessage());
        }
    }
}