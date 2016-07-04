package ua.com.juja.garazd.sqlcmd.controller;

import ua.com.juja.garazd.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.Concole;
import ua.com.juja.garazd.sqlcmd.view.View;

public class Main {
    public static void main(String[] args) {
        View view = new Concole();
        JDBCDatabaseManager manager = new JDBCDatabaseManager();

        MainController controller = new MainController(view, manager);
        controller.run();
    }
}