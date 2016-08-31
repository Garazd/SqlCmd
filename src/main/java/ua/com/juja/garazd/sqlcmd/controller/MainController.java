package ua.com.juja.garazd.sqlcmd.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.juja.garazd.sqlcmd.controller.command.ClearTable;
import ua.com.juja.garazd.sqlcmd.controller.command.Command;
import ua.com.juja.garazd.sqlcmd.controller.command.CreateTable;
import ua.com.juja.garazd.sqlcmd.controller.command.Exit;
import ua.com.juja.garazd.sqlcmd.controller.command.ExitException;
import ua.com.juja.garazd.sqlcmd.controller.command.Find;
import ua.com.juja.garazd.sqlcmd.controller.command.Help;
import ua.com.juja.garazd.sqlcmd.controller.command.Tables;
import ua.com.juja.garazd.sqlcmd.controller.command.Unsupported;
import ua.com.juja.garazd.sqlcmd.controller.properties.Configuration;
import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;

public class MainController {

    Logger logger = LogManager.getLogger(MainController.class.getName());

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
            new ClearTable(manager, view),
            new CreateTable(manager, view),
            new Find(manager, view),
            new Unsupported(view)
        };
    }

    public void run() {
        try {
            if (!connectionDatabase()) {
                return;
            }
            doWork();
        } catch (ExitException e) {
            logger.debug("Error in the method run " + e);
        }
    }

    private boolean connectionDatabase() {
        while (!manager.isConnected()) {
            try {
                Configuration configuration = new Configuration();
                String databaseName = configuration.getDatabaseName();
                String userName = configuration.getUserName();
                String password = configuration.getPassword();
                manager.connectDatabase(databaseName, userName, password);
            } catch (Exception e) {
                logger.debug("Error in the method connectionDatabase " + e);
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
                        logger.debug("Error in the method doWork " + e);
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