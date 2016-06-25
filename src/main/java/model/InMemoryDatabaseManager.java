package model;

import java.util.*;

public class InMemoryDatabaseManager implements DatabaseManager {

    private Map<String, List<DataSet>> tables = new LinkedHashMap<>();

    @Override
    public void create(String tableName, DataSet input) {
        get(tableName).add(input);
    }

    @Override
    public void update(String tableName, int id, DataSetImpl newValue) {
        for (DataSet dataSet : get(tableName)) {
            if ((int)dataSet.get("id") == id) {
                dataSet.updateFrom(newValue);
            }
        }
    }

    @Override
    public void clear(String tableName) {
        get(tableName).clear();
    }

    @Override
    public Set<String> getTableNames() {
        return tables.keySet();
    }

    @Override
    public List<DataSet> getTableData(String tableName) {
        return get(tableName);
    }

    @Override
    public int getSize(String tableName) {
        return get(tableName).size();
    }

    @Override
    public void connect(String database, String userName, String password) {
        //do nothing
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        return new LinkedHashSet<String>(Arrays.asList("name", "password", "id"));
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    private List<DataSet> get(String tableName) {
        if (!tables.containsKey(tableName)) {
            tables.put(tableName, new LinkedList<DataSet>());
        }
        return tables.get(tableName);
    }
}