package ua.com.juja.garazd.sqlcmd.controller.command;

import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;

public class CreateDatabase implements Command {
    private DatabaseManager manager;
    private View view;

    public CreateDatabase(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("createDatabase|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException(String.format("Must be an even number of parameters in a format " +
                "'createEntry|tableName|column1|value1|column2|value2|...|columnN|valueN', " +
                "but you sent: '%s'", command));
        }

        manager.createDatabase(data[1]);
        view.write("Base '" + data[1] + "' created.");
    }
}