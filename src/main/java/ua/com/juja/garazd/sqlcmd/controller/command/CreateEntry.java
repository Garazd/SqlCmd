package ua.com.juja.garazd.sqlcmd.controller.command;

import java.util.LinkedHashMap;
import java.util.Map;
import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;

public class CreateEntry implements Command {
    private DatabaseManager manager;
    private View view;

    public CreateEntry(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("createEntry|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length %2 != 0) {
            throw new IllegalArgumentException(String.format("Must be an even number of parameters in a format " +
                "'createEntry|tableName|column1|value1|column2|value2|...|columnN|valueN', " +
                "but you sent: '%s'", command));
        }

        String tableName = data[1];

        Map<String, Object> tableData = new LinkedHashMap<>();
        for (int index = 1; index < (data.length / 2); index++) {
            String columnName = data[index * 2];
            String value = data[index * 2 + 1];

            tableData.put(columnName, value);
        }
        manager.createEntry(tableName, tableData);

        view.write(String.format("Recording %s was successfully created in the table '%s'.", tableData, tableName));
    }
}