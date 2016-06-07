package controller.command;

import model.DatabaseManager;
import view.View;

public class Clear implements Command {

    private DatabaseManager manager;
    private View view;

    public Clear(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("clear|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("command format is 'clear|tableName', and you have brought: " + command);
        }
        manager.clear(data[1]);

        view.write(String.format("Table %s has been successfully cleared.", data[1]));
    }
}