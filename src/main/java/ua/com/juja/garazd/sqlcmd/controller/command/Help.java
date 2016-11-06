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
        view.write("\t\tto show all commands");

        view.write("\tdatabases");
        view.write("\t\tto to show all databases");

        view.write("\tcreateDatabase|databaseName");
        view.write("\t\tto create the database");

        view.write("\tdropDatabase|databaseName");
        view.write("\t\tto drop the database");

        view.write("\tcreateTable|tableName(columnName1 type, columnName2 type,...columnNameN type)");
        view.write("\t\tto create the table with the name. Type can be text or integer");

        view.write("\tdropTable|tableName");
        view.write("\t\tto drop the table");

        view.write("\tcreateEntry|tableName|column1|value1|column2|value2|...|columnN|valueN");
        view.write("\t\tto create entries the table");

        view.write("\tclearTable|tableName");
        view.write("\t\tto clear the table");

        view.write("\tshow");
        view.write("\t\tto show all tables in the database");

        view.write("\tcontents|tableName");
        view.write("\t\tto get the contents of the table");

        view.write("\texit");
        view.write("\t\tto exit the program");
    }
}