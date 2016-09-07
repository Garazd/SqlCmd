package ua.com.juja.garazd.sqlcmd.controller;

import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.juja.garazd.sqlcmd.controller.command.ClearTable;
import ua.com.juja.garazd.sqlcmd.controller.command.Command;
import ua.com.juja.garazd.sqlcmd.controller.command.CreateDatabase;
import ua.com.juja.garazd.sqlcmd.controller.command.CreateEntry;
import ua.com.juja.garazd.sqlcmd.controller.command.CreateTable;
import ua.com.juja.garazd.sqlcmd.controller.command.DropDatabase;
import ua.com.juja.garazd.sqlcmd.controller.command.DropTable;
import ua.com.juja.garazd.sqlcmd.controller.command.Exit;
import ua.com.juja.garazd.sqlcmd.controller.command.ExitException;
import ua.com.juja.garazd.sqlcmd.controller.command.GetTableData;
import ua.com.juja.garazd.sqlcmd.controller.command.GetTablesNames;
import ua.com.juja.garazd.sqlcmd.controller.command.Help;
import ua.com.juja.garazd.sqlcmd.controller.command.Unsupported;
import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;
import ua.com.juja.garazd.sqlcmd.view.View;

public class MainController {

    Logger logger = LogManager.getLogger(MainController.class.getName());

    private DatabaseManager manager;
    private View view;
    private List<Command> commands;

    public MainController(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;

        commands = Arrays.asList(
            new Help(view),
            new Exit(view),
            new CreateDatabase(manager, view),
            new DropDatabase(manager, view),
            new CreateTable(manager, view),
            new DropTable(manager, view),
            new CreateEntry(manager, view),
            new ClearTable(manager, view),
            new GetTablesNames(manager, view),
            new GetTableData(manager, view),
            new Unsupported(view));
    }

    public void run() {
        try {
            doWork();
        } catch (ExitException e) {
            logger.debug("Error in the method run " + e);
        }
    }

    private void doWork() {
        welcomeSQLCmd();

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
                        break;
                    }
                    printError(e);
                    break;
                }
            }
        }
    }

    private void welcomeSQLCmd() {
        view.write("===========================================");
        view.write("============ Welcome to SQLCmd ============");
        view.write("===========================================");
        view.write("To display commands, enter the command help");
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