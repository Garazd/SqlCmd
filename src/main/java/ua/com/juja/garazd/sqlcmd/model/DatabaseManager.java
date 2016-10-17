package ua.com.juja.garazd.sqlcmd.model;

import java.util.List;
import java.util.Set;

public interface DatabaseManager {

    void connectDatabase(String database, String userName, String password);

    boolean isConnected();


    void createDatabase (String databaseName);

    void dropDatabase (String databaseName);

    void createTable(String query);

    void dropTable(String tableName);

    void createEntry(String tableName, DataSet input);

    void clearTable(String tableName);

    void updateTable(String tableName, int id, DataSetImpl newValue);


    Set<String> getTableNames();

    List<DataSet> getTableData(String tableName);

    Set<String> getTableColumns(String tableName);
}