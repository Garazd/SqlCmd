package controller;

import model.JDBCDatabaseManager;
import view.Concole;
import view.View;

public class Main {
    public static void main(String[] args) {
        View view = new Concole();
        JDBCDatabaseManager manager = new JDBCDatabaseManager();

        MainController controller = new MainController(view, manager);
        controller.run();
    }
}