package ua.com.juja.garazd.sqlcmd.controller.command;

import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;

public class DropTable implements Command {
    private DatabaseManager manager;
    private View view;

    public DropTable(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }
    @Override
    public boolean canProcess(String command) {
        return command.startsWith("dropTable|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Command format is 'dropTable|tableName', and you input: " + command);
        }
        manager.dropTable(data[1]);
        view.write("Table '" + data[1] + "' dropped.");
    }
}