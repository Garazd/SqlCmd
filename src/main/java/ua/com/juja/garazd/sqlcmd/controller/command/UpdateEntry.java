package ua.com.juja.garazd.sqlcmd.controller.command;

import java.util.LinkedHashMap;
import java.util.Map;
import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;

public class UpdateEntry implements Command {

    private DatabaseManager manager;
    private View view;

    public UpdateEntry(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("updateEntry|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length % 2 == 0) {
            throw new IllegalArgumentException(String.format("Must be an odd number of parameters in a format " +
                "'updateEntry|tableName|idColumn|columnName1|newValue1|columnName2|newValue2|...|columnNameN|newValueN', " +
                "but you sent: '%s'", command));
        }

        String tableName = data[1];
        int id = Integer.parseInt(data[2]);

        Map<String, Object> tableData = new LinkedHashMap<>();
        for (int index = 1; index < (data.length / 2); index++) {
            String columnName = data[index * 2 + 1];
            String value = data[index * 2 + 2];

            tableData.put(columnName, value);
        }
        manager.updateTable(tableName, id, tableData);

        view.write(String.format("Recording %s in the table '%s' successfully updated.", id, tableName));
    }
}