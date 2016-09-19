package ua.com.juja.garazd.sqlcmd.controller.command;

import ua.com.juja.garazd.sqlcmd.controller.properties.Configuration;
import ua.com.juja.garazd.sqlcmd.model.DatabaseConnection;
import ua.com.juja.garazd.sqlcmd.view.View;

public class ConnectDatabase implements Command {

    private Configuration configuration = new Configuration();
    private DatabaseConnection connection;
    private View view;

    public ConnectDatabase(DatabaseConnection connection, View view) {
        this.connection = connection;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect");
    }

    @Override
    public void process(String command) {
        connection.connectDatabase(
            configuration.getDatabaseName(),
            configuration.getUserName(),
            configuration.getPassword());

        view.write("Success!");
    }
}