package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class JDBCDatabaseManager implements DatabaseManager {

    private Connection connection;

    @Override
    public void create(String tableName, DataSet input) {
        try (Statement statement = connection.createStatement())
        {
            String tableNames = getNameFormatted(input, "%s,");
            String values = getValueFormatted(input, "'%s',");

            statement.executeUpdate("INSERT INTO public." + tableName + " (" + tableNames + ")" +
                "VALUES (" + values + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String tableName, int id, DataSetImpl newValue) {

        String tableNames = getNameFormatted(newValue, "%s = ?,");
        String sql = "UPDATE public." + tableName + " SET " + tableNames +
            " WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            int index = 1;
            for (Object value : newValue.getValues()) {
                preparedStatement.setObject(index , value);
                index++;
            }
            preparedStatement.setObject(index, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear(String tableName) {
        try (Statement statement = connection.createStatement())
        {
            statement.executeUpdate("DELETE FROM public." + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getTableNames() {
        Set<String> tables = new LinkedHashSet<String>();
        try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery
            ("SELECT table_name FROM information_schema.tables " +
            "WHERE table_schema='public' AND table_type = 'BASE TABLE'"))
        {
            while (resultSet.next()) {
                tables.add(resultSet.getString("table_name"));
            }
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            return tables;
        }
    }

    @Override
    public List<DataSet> getTableData(String tableName) {
        List<DataSet> result = new LinkedList<DataSet>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM public." + tableName))
        {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int index = 0;
            while (resultSet.next()) {
                DataSet dataSet = new DataSetImpl();
                result.add(dataSet);
                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    dataSet.put(resultSetMetaData.getColumnName(i + 1), resultSet.getObject(i + 1));
                }
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    @Override
    public void connect(String database, String userName, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Please add JDBC jar to project.");
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(
                "jdbs:postgresql://localhost:5432/" + database, userName, password);
        } catch (SQLException e) {
            System.out.println(String.format("Cant get connection for database:%s user:%s", database, userName));
            e.printStackTrace();
            connection = null;
        }
    }

    private static String getNameFormatted(DataSet newValue, String format) {
        String string = "";
        for (String name : newValue.getNames()) {
            string += String.format(format, name);
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }

    private String getValueFormatted(DataSet input, String format) {
        String values = "";
        for (Object value : input.getValues()) {
            values += String.format(format, value);
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

    @Override
    public int getSize(String tableName) {
        try (Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM public." + tableName);
            resultSet.next();
            int size = resultSet.getInt(1);
            return size;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        Set<String> tables = new LinkedHashSet<String>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                 "SELECT * FROM information_schema.columns " +
                 "WHERE table_schema = 'public' AND table_name = '" + tableName + "'"))
        {
            while (resultSet.next()) {
                tables.add(resultSet.getString("column_name"));
            }
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            return tables;
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }
}