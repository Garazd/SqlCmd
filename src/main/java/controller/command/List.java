package controller.command;

import java.util.Arrays;
import model.DatabaseManager;
import view.View;

public class List implements Command {

    private DatabaseManager manager;
    private View view;

    public List(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("list");
    }

    @Override
    public void process(String command) {
        String[] tableNames = manager.getTableNames();

        String massage = Arrays.toString(tableNames);

        view.write(massage);
    }
}