package ua.com.juja.garazd.sqlcmd.controller.command;

import java.util.Set;
import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;

public class GetTablesNames implements Command {
    private DatabaseManager manager;
    private View view;

    public GetTablesNames(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("list");
    }

    @Override
    public void process(String command) {
        Set<String> tableNames = manager.getTableNames();

        String massage = tableNames.toString();

        view.write(massage);
    }
}