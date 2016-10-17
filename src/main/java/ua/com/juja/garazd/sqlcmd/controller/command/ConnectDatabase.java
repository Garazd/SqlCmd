package ua.com.juja.garazd.sqlcmd.controller.command;

import ua.com.juja.garazd.sqlcmd.controller.properties.Configuration;
import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;

public class ConnectDatabase implements Command {

    private Configuration configuration = new Configuration();
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
        manager.connectDatabase(
            configuration.getDatabaseName(),
            configuration.getUserName(),
            configuration.getPassword());

        view.write("Success!");
    }
}