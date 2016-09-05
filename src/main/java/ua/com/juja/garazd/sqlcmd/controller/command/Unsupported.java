package ua.com.juja.garazd.sqlcmd.controller.command;

import ua.com.juja.garazd.sqlcmd.view.View;

public class Unsupported implements Command {
    private View view;

    public Unsupported(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(final String command) {
        return true;
    }

    @Override
    public void process(final String command) {
        view.write("Command: " + command + " does not exist");
        view.write("Enter command (or help for help):");
    }
}