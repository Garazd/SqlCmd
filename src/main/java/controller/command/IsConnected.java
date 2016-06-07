package controller.command;

import model.DatabaseManager;
import view.View;

public class IsConnected implements Command {

    private DatabaseManager manager;
    private View view;

    public IsConnected(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return !manager.isConnected();
    }

    @Override
    public void process(String command) {
        view.write(String.format("You can not use the command '%s' is until " +
            "you connect using commands connect|databaseName|userName|password", command));
    }
}