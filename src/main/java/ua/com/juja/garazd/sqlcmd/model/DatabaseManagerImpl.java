package ua.com.juja.garazd.sqlcmd.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseManagerImpl implements DatabaseManager {

    Logger logger = LogManager.getLogger(DatabaseManagerImpl.class.getName());
    private DatabaseConnectionImpl databaseConnectionImpl = new DatabaseConnectionImpl();
    private Connection connection = databaseConnectionImpl.getConnection();

    @Override
    public void createDatabase(String databaseName) {
        executeUpdate("CREATE DATABASE " + databaseName);
    }

    @Override
    public void dropDatabase(String databaseName) {
        executeUpdate("DROP DATABASE IF EXISTS " + databaseName);
    }

    private void executeUpdate(String sql) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            try {
                throw new SQLException("Error update data in case - %s\n", e);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void createTable(String tableName, DataSet input) {
        try (Statement statement = connection.createStatement()) {
            String tableNames = getNameFormatted(input, "%s,");
            String values = getValueFormatted(input, "'%s',");

            statement.executeUpdate("INSERT INTO public." + tableName + " (" + tableNames + ")" +
                "VALUES (" + values + ")");
        } catch (SQLException e) {
            logger.debug("Error in the method createTable " + e);
        }
    }

    @Override
    public void dropTable(String tableName) {
        dropSequence(tableName);
        executeUpdate(String.format("DROP TABLE IF EXISTS public.%s CASCADE", tableName));
    }

    private void dropSequence(String tableName) {
        executeUpdate(String.format("DROP SEQUENCE IF EXISTS public.%s_seq CASCADE", tableName));
    }

    @Override
    public void updateTable(String tableName, int id, DataSetImpl newValue) {

        String tableNames = getNameFormatted(newValue, "%s = ?,");
        String sql = "UPDATE public." + tableName + " SET " + tableNames +
            " WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int index = 1;
            for (Object value : newValue.getValues()) {
                preparedStatement.setObject(index, value);
                index++;
            }
            preparedStatement.setObject(index, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.debug("Error in the method updateTable " + e);
        }
    }

    @Override
    public void clearTable(String tableName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM public." + tableName);
        } catch (SQLException e) {
            logger.debug("Error in the method clearTable " + e);
        }
    }

    @Override
    public Set<String> getTableNames() {
        Set<String> tables = new LinkedHashSet<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery
                 ("SELECT table_name FROM information_schema.tables " +
                     "WHERE table_schema='public' AND table_type = 'BASE TABLE'")) {
            while (resultSet.next()) {
                tables.add(resultSet.getString("table_name"));
            }
            return tables;
        } catch (SQLException e) {
            logger.debug("Error in the method getTableNames " + e);
            return tables;
        }
    }

    @Override
    public List<DataSet> getTableData(String tableName) {
        List<DataSet> result = new LinkedList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM public." + tableName)) {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            while (resultSet.next()) {
                DataSet dataSet = new DataSetImpl();
                result.add(dataSet);
                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    dataSet.put(resultSetMetaData.getColumnName(i + 1), resultSet.getObject(i + 1));
                }
            }
            return result;
        } catch (SQLException e) {
            logger.debug("Error in the method getTableData " + e);
            return result;
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
    public Set<String> getTableColumns(String tableName) {
        Set<String> tables = new LinkedHashSet<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                 "SELECT * FROM information_schema.columns " +
                     "WHERE table_schema = 'public' AND table_name = '" + tableName + "'")) {
            while (resultSet.next()) {
                tables.add(resultSet.getString("column_name"));
            }
            return tables;
        } catch (SQLException e) {
            logger.debug("Error in the method getTableColumns " + e);
            return tables;
        }
    }
}