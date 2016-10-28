package ua.com.juja.garazd.sqlcmd.controller.command;

import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;

public class ConnectDatabase implements Command {

    private int NUMBER_OF_PARAMETERS = 4;
    private DatabaseManager manager;
    private View view;

    public ConnectDatabase(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != NUMBER_OF_PARAMETERS) {
            throw new IllegalArgumentException("Invalid number of parameters separated by " +
                        "sign '|', expected connect|databaseName|userName|password, and you input: " + command);
        }
        String database = data[1];
        String userName = data[2];
        String password = data[3];

        manager.connectDatabase(database, userName, password);

        view.write("Success!");
    }
}