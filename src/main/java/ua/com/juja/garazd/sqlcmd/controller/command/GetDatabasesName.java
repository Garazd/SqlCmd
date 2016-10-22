package ua.com.juja.garazd.sqlcmd.controller.command;

import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;

public class GetDatabasesName implements Command {

    private final DatabaseManager manager;
    private final View view;

    public GetDatabasesName(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("databases");
    }

    @Override
    public void process(String command) {
        view.write("=====Databases=====");
        manager.getDatabasesName().forEach(view::write);
    }
}