package ua.com.juja.garazd.sqlcmd.controller;

import ua.com.juja.garazd.sqlcmd.controller.command.*;
import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;

public class MainController {

    private DatabaseManager manager;
    private View view;
    private Command[] commands;

    public MainController(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
        this.commands = new Command[]{
            new Help(view),
            new Exit(view),
            new Tables(manager, view),
            new Clear(manager, view),
            new Create(manager, view),
            new Find(manager, view),
            new Unsupported(view)
        };
    }

    public void run() {

        if (!connectionDatabase()) {
            return;
        }
        doWork();
    }

    private boolean connectionDatabase() {
        while (!manager.isConnected()) {
            view.write("Enter the database name: ");
            String databaseName = view.read();
            view.write("Enter user name: ");
            String userName = view.read();
            view.write("Enter password: ");
            String password = view.read();
            try {
                manager.connect(databaseName, userName, password);
            } catch (Exception e) {
                printError(e);
            }
            if (!manager.isConnected()) {
                view.write("To retry? (y/n):");
                String input = view.read();
                if (!input.equalsIgnoreCase("y")) {
                    view.write("See you later! Bye");
                    return false;
                }
            }
        }
        view.write("Connecting to a database is successful");
        return true;
    }

    private void doWork() {
        view.write("Enter command (or help for help):");

        while (true) {
            String input = view.read();

            for (Command command : commands) {
                try {
                    if (command.canProcess(input)) {
                        command.process(input);
                        break;
                    }
                } catch (Exception e) {
                    if (e instanceof ExitException) {
                        throw e;
                    }
                    printError(e);
                    break;
                }
            }
        }
    }

    private void printError(Exception e) {
        String massage = e.getMessage();
        Throwable cause = e.getCause();
        if (cause != null) {
            massage += " " + cause.getMessage();
        }
        view.write("Failure! because of: " + massage);
        view.write("Please try again.");
    }
}