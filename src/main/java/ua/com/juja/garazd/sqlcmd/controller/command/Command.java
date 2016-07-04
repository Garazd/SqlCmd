package ua.com.juja.garazd.sqlcmd.controller.command;

public interface Command {

    boolean canProcess(String command);

    void process(String command);
}