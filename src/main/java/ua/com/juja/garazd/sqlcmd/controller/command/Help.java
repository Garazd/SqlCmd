package ua.com.juja.garazd.sqlcmd.controller.command;

import ua.com.juja.garazd.sqlcmd.view.View;

public class Help implements Command {
    private View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {
        view.write("Existing command:");

        view.write("\thelp");
        view.write("\t\tto display the commands list on the screen");

        view.write("\tcreateDatabase|databaseName");
        view.write("\t\tto create the database with the name");

        view.write("\tdropDatabase|databaseName");
        view.write("\t\tto drop the database named");

        view.write("\tcreateTable|tableName");
        view.write("\t\tto create the table with the name");

        view.write("\tdropTable|tableName");
        view.write("\t\tto drop the table named");

        view.write("\tcreateEntry|tableName|column1|value1|column2|value2|...|columnN|valueN");
        view.write("\t\tto create entries in the table");

        view.write("\tclearTable|tableName");
        view.write("\t\tto clean up the entries table");

        view.write("\tshow");
        view.write("\t\tfor a show of all tables, existing to the database");

        view.write("\tcontents|tableName");
        view.write("\t\tto get the contents of the table 'tableName'");

        view.write("\texit");
        view.write("\t\tto exit from the program");
    }
}