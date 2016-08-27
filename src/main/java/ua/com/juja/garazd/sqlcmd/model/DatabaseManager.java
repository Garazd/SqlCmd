package ua.com.juja.garazd.sqlcmd.model;

import java.util.List;
import java.util.Set;

public interface DatabaseManager {

    void createTable(String tableName, DataSet input);

    void updateTable(String tableName, int id, DataSetImpl newValue);

    void clearTable(String tableName);

    Set<String> getTableNames();

    List<DataSet> getTableData(String tableName);

    int getSize(String tableName);

    void connectDatabase(String database, String userName, String password);

    Set<String> getTableColumns(String tableName);

    boolean isConnected();
}