package controller;

import java.util.Arrays;
import model.DataSet;
import model.JDBCDatabaseManager;
import view.View;

public class MainController {
    private View view;
    private JDBCDatabaseManager manager;

    public MainController(View view, JDBCDatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    public void run() {
        connectToDB();

        while (true) {
            view.write("Enter command (or help for help):");
            String command = view.read();

            if (command.equals("list")) {
                doList();
            } else if (command.equals("help")) {
                doHelp();
            } else if (command.equals("exit")) {
                System.out.println("See you soon!");
                System.exit(0);
            } else if (command.startsWith("find|")) {
                doFind(command);
            } else {
                view.write("Nonexistent command: " + command);
            }
        }
    }

    private void doFind(String command) {
        String[] data = command.split("\\|");
        String tableName = data[1];

        String[] tableColumns = manager.getTableColumns(tableName);
        printHeader(tableColumns);

        DataSet[] tableData = manager.getTableData(tableName);
        printTable(tableData);
    }

    private void printTable(DataSet[] tableData) {
        for (DataSet row : tableData) {
            printRow(row);
        }
    }

    private void printRow(DataSet row) {
        Object[] values = row.getValues();
        String result = "|";
        for (Object value : values) {
            result += value + "|";
        }
        view.write(result);
    }

    private void printHeader(String[] tableColmns) {
        String result = "|";
        for (String name : tableColmns) {
            result += name + "|";
        }
        view.write("---------------------");
        view.write(result);
        view.write("---------------------");
    }

    private void doHelp() {
        view.write("Current team:");
        view.write("\tlist");
        view.write("\t\tfor a list of all database tables, which are connected to the");

        view.write("\tfind|tableName");
        view.write("\t\tfor 'tableName' table contents");

        view.write("\thelp");
        view.write("\t\tto display the list on the screen");

        view.write("\texit");
        view.write("\t\tto exit from the program");
    }

    private void doList() {
        String[] tableNames = manager.getTableNames();

        String massage = Arrays.toString(tableNames);

        view.write(massage);
    }

    private void connectToDB() {
        view.write("Hello user!");
        view.write("Please enter the database name, user name and password in the format: database|userName|password");

        while (true) {
            try {
                String string = view.read();
                String[] data = string.split("\\|");
                if (data.length != 3) {
                    throw new IllegalAccessException("Invalid number of parameters separated by '|', is expected to 3, but there is:" + data.length);
                }
                String databaseName = data[0];
                String userName = data[1];
                String password = data[2];

                manager.connect(databaseName, userName, password);
                break;
            } catch (Exception e) {
                printError(e);
            }
        }

        view.write("Success!");
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