package controller.command;

public interface Command {

    boolean canProcess(String command);

    void process(String command);
}