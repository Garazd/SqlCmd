package ua.com.juja.garazd.sqlcmd.controller.command;

import ua.com.juja.garazd.sqlcmd.model.DatabaseConnection;
import ua.com.juja.garazd.sqlcmd.view.View;

public class IsConnected implements Command {

    private DatabaseConnection connection;
    private View view;

    public IsConnected(DatabaseConnection connection, View view) {
        this.connection = connection;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return !connection.isConnected();
    }

    @Override
    public void process(String command) {
        view.write(String.format("You can not use the command '%s' " +
            "is until you join using command 'connect'", command));
    }
}