package ua.com.juja.garazd.sqlcmd.controller.command;

import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;

public class ClearTable implements Command {
    private DatabaseManager manager;
    private View view;

    public ClearTable(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("clearTable|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Command format is 'clearTable|tableName', and you input: " + command);
        }
        String clearTableName = data[1];
        confirmAndClearTable(clearTableName);
    }
    
    private void confirmAndClearTable(String clearTableName) {
        try {
            view.write(String.format("Delete the data from the table '%s'. Y/N", clearTableName));
            if (view.read().equalsIgnoreCase("y")) {
                manager.clearTable(clearTableName);
                view.write(String.format("Table %s has been successfully cleared.", clearTableName));
            } else {
                view.write("Table data will not removed");
            }
        } catch (Exception e) {
            view.write(String.format("Failed to delete the table '%s', because: %s", clearTableName, e.getMessage()));
        }
    }
}