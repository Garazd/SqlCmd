package ua.com.juja.garazd.sqlcmd.model;

public interface DatabaseConnection {

    void connectDatabase(String database, String userName, String password);

    boolean isConnected();
}