package model;

public interface DatabaseManager {

    void create(String tableName, DataSet input);

    void update(String tableName, int id, DataSet newValue);

    void clear(String tableName);

    String[] getTableNames();

    DataSet[] getTableData(String tableName);

    void connect(String database, String userName, String password);

    String[] getTableColumns(String tableName);

    boolean isConnected();
}