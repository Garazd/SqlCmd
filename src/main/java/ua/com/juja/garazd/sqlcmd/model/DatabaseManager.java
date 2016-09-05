package ua.com.juja.garazd.sqlcmd.model;

import java.util.List;
import java.util.Set;

public interface DatabaseManager {

    void createDatabase (String databaseName);

    void dropDatabase (String databaseName);

    void createTable(String tableName, DataSet input);

    void dropTable(String tableName);

    void updateTable(String tableName, int id, DataSetImpl newValue);

    void clearTable(String tableName);

    Set<String> getTableNames();

    List<DataSet> getTableData(String tableName);

    Set<String> getTableColumns(String tableName);
}