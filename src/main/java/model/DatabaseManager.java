package model;

import java.util.List;
import java.util.Set;

public interface DatabaseManager {

    void create(String tableName, DataSet input);

    void update(String tableName, int id, DataSetImpl newValue);

    void clear(String tableName);

    Set<String> getTableNames();

    List<DataSet> getTableData(String tableName);

    int getSize(String tableName);

    void connect(String database, String userName, String password);

    Set<String> getTableColumns(String tableName);

    boolean isConnected();
}