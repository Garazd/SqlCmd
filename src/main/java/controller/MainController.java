package controller;

import controller.command.*;
import model.DatabaseManager;
import view.View;

public class MainController {

    private Command[] commands;
    private View view;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.commands = new Command[] {
            new Connect(manager, view),
            new Help(view),
            new Exit(view),
            new IsConnected(manager, view),
            new Tables(manager, view),
            new Clear(manager, view),
            new Create(manager, view),
            new Find(manager, view),
            new Unsupported(view)
        };
    }

    public void run() {
        try {
            doWork();
        } catch (ExitException e) {
            //do nothing
        }
    }

    private void doWork() {
        view.write("Hello user!");
        view.write("Please enter a database name, username and password in the format: connect|database|userName|password");

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
            view.write("Enter command (or help for help):");
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