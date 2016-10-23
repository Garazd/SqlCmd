package ua.com.juja.garazd.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GetDatabasesNamesTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new GetDatabasesNames(manager, view);
    }

    @Test
    public void testCanProcessGetDatabasesNamesParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("databases");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessGetDatabasesNamesWithoutParametersString() {
        // given

        // when
        boolean canProcess = command.canProcess("");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessSomeString() {
        // given

        // when
        boolean canProcess = command.canProcess("databases|user");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testProcessCommandGetDatabasesNames() {
        // given

        // when
        command.process("databases");

        // then
        verify(view).write("=====Databases=====");
    }
}