package controller.command;

import view.View;

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
        view.write("Current command:");

        view.write("\tconnect|databaseName|userName|password");
        view.write("\t\tto connect to a database, which will work");

        view.write("\tlist");
        view.write("\t\tfor a list of all database tables, is connected to");

        view.write("\tclear|tableName");
        view.write("\t\tto clean up the entire table");

        view.write("\tcreate|tableName|column1|value1|column2|value2|...|columnN|valueN");
        view.write("\t\tto create a record in the table");

        view.write("\tfind|tableName");
        view.write("for the contents of the tableName");

        view.write("\thelp");
        view.write("\t\tto display the list on the screen");

        view.write("\texit");
        view.write("\t\tto exit from the program");
    }
}