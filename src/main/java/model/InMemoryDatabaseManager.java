package model;

import java.util.Arrays;

public class InMemoryDatabaseManager implements DatabaseManager {

    public static final String TABLE_NAME = "user";

    private DataSet[] data = new DataSet[1000];
    private int freeIndex = 0;

    @Override
    public void create(String tableName, DataSet input) {
        validateTable(tableName);

        data[freeIndex] = input;
        freeIndex++;
    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {
        validateTable(tableName);

        for (int index = 0; index < freeIndex; index++) {
            if ((int)data[index].get("id") == id) {
                data[index].updateFrom(newValue);
            }
        }
    }

    @Override
    public void clear(String tableName) {
        validateTable(tableName);

        data = new DataSet[1000];
        freeIndex = 0;
    }

    @Override
    public String[] getTableNames() {
        return new String[] { TABLE_NAME };
    }

    @Override
    public DataSet[] getTableData(String tableName) {
        validateTable(tableName);

        return Arrays.copyOf(data, freeIndex);
    }

    @Override
    public void connect(String database, String userName, String password) {
        //do nothing
    }

    @Override
    public String[] getTableColumns(String tableName) {
        return new String[] {"name", "password", "id"};
    }

    private void validateTable(String tableName) {
        if (!"user".equals(tableName)) {
            throw new UnsupportedOperationException("\"Only for 'user' table, but you try to work with: " + tableName);
        }
    }
}