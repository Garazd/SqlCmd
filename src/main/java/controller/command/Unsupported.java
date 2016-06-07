package controller.command;

import view.View;

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
        view.write("Nonexistent command: " + command);
    }
}