package ua.com.juja.garazd.sqlcmd.controller.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import ua.com.juja.garazd.sqlcmd.model.DataSet;
import ua.com.juja.garazd.sqlcmd.model.DataSetImpl;
import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.garazd.sqlcmd.view.View;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetTableDataTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new GetTableData(manager, view);
    }

    @Test
    public void testPrintTableData() {
        //given
        setupTableColumns("user", "id", "name", "password");

        DataSet user1 = new DataSetImpl();
        user1.put("id", 12);
        user1.put("name", "Stiven");
        user1.put("password", "*****");

        DataSet user2 = new DataSetImpl();
        user2.put("id", 13);
        user2.put("name", "Eva");
        user2.put("password", "+++++");

        when(manager.getTableData("user")).thenReturn(Arrays.asList(user1, user2));

        //when
        command.process("find|user");

        //then
        shouldPrint("[--------------------, " +
                    "|id|name|password|, " +
                    "--------------------, " +
                    "|12|Stiven|*****|, " +
                    "|13|Eva|+++++|, " +
                    "--------------------]");
    }

    private void setupTableColumns(String tableName, String... columns) {
        when(manager.getTableColumns(tableName))
            .thenReturn(new LinkedHashSet<>(Arrays.asList(columns)));
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessFindWithParametersString() {
        //given

        //when
        boolean canProcess = command.canProcess("contents|user");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFindWithoutParametersString() {
        //given

        //when
        boolean canProcess = command.canProcess("contents");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessQweString() {
        //given

        //when
        boolean canProcess = command.canProcess("qwe|user");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testPrintEmptyTableData() {
        //given
        setupTableColumns("user", "id", "name", "password");

        when(manager.getTableData("user"))
            .thenReturn(new ArrayList<>());

        //when
        command.process("find|user");

        //then
        shouldPrint("[--------------------, " +
                    "|id|name|password|, " +
                    "--------------------, " +
                    "--------------------]");
    }

    @Test
    public void testPrintTableDataWithOneColumn() {
        //given
        setupTableColumns("test", "id");

        DataSet user1 = new DataSetImpl();
        user1.put("id", 12);

        DataSet user2 = new DataSetImpl();
        user2.put("id", 13);

        when(manager.getTableData("test")).
            thenReturn(Arrays.asList(user1, user2));

        //when
        command.process("find|test");

        //then
        shouldPrint("[--------------------, " +
            "|id|, " +
            "--------------------, " +
            "|12|, " +
            "|13|, " +
            "--------------------]");
    }
}