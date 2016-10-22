package ua.com.juja.garazd.sqlcmd.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.juja.garazd.sqlcmd.controller.properties.Configuration;

public class DatabaseManagerImpl implements DatabaseManager {

    private static Configuration configuration = new Configuration();
    private static String USER_NAME = configuration.getUserName();
    private static String PASSWORD = configuration.getPassword();
    private static Logger logger = LogManager.getLogger(DatabaseManagerImpl.class.getName());
    private Connection connection;

    static {
        try {
            Class.forName(configuration.getClassDriver());
        } catch (ClassNotFoundException e) {
            System.out.println("Please load database driver to project.");
            logger.debug("Error in the load load database driver to project " + e);
        }
    }

    @Override
    public void connectDatabase(String databaseName, String userName, String password) {
        try {
            Configuration configuration = new Configuration();
            String databaseUrl = String.format("%s%s:%s/%s",
                configuration.getJdbcDriver(),
                configuration.getServerName(),
                configuration.getPortNumber(),
                configuration.getDatabaseName());
            connection = DriverManager.getConnection(databaseUrl, USER_NAME, PASSWORD);
        } catch (Exception e) {
            connection = null;
            logger.debug("Error in the method connectionDatabase do not correct values in the file configuration " + e);
            throw new RuntimeException("Please enter the correct values in the file configuration.");
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void createDatabase(String databaseName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE " + databaseName);
        } catch (SQLException e) {
            logger.debug("Error in the method createDatabase " + e);
        }
    }

    @Override
    public void dropDatabase(String databaseName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP DATABASE " + databaseName);
        } catch (SQLException e) {
            logger.debug("Error in the method dropDatabase " + e);
        }
    }

    private void executeUpdate(String sql) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            try {
                throw new SQLException("Error update data in case - %s\n", e);
            } catch (SQLException e1) {
                logger.debug("Error in the method executeUpdate " + e);
            }
        }
    }

    @Override
    public void createTable(String query) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + query);
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
    public void createEntry(String tableName, Map<String, Object> input) {
        String tableNames = getNameFormatted(input, "%s,");
        String values = getValueFormatted(input, "'%s',");

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO public." + tableName + " (" + tableNames + ")" +
                "VALUES (" + values + ")");
        } catch (SQLException e) {
            logger.debug("Error in the method createEntry " + e);
        }
    }

    @Override
    public void updateTable(String tableName, int id, Map<String, Object> newValue) {
        String tableNames = getNameFormatted(newValue, "%s = ?,");
        String sqlQuery = "UPDATE public." + tableName + " SET " + tableNames +
            " WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            int index = 1;
            for (Object value : newValue.keySet()) {
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
    public List<Map<String, Object>> getTableData(String tableName) {
        List<Map<String, Object>> result = new LinkedList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM public." + tableName)) {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            while (resultSet.next()) {
                Map<String, Object> data = new LinkedHashMap<>();
                for (int index = 1; index <= resultSetMetaData.getColumnCount(); index++) {
                    data.put(resultSetMetaData.getColumnName(index), resultSet.getObject(index));
                }
                result.add(data);
            }
            return result;
        } catch (SQLException e) {
            logger.debug("Error in the method getTableData " + e);
            return result;
        }
    }

    private static String getNameFormatted(Map<String, Object> newValue, String format) {
        String string = "";
        for (String name : newValue.keySet()) {
            string += String.format(format, name);
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }

    private String getValueFormatted(Map<String, Object> input, String format) {
        String values = "";
        for (Object value : input.keySet()) {
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

    @Override
    public Set<String> getDatabasesName() {
        connectDatabase("", USER_NAME, PASSWORD);
        String sqlQuery = "SELECT datname FROM pg_database WHERE datistemplate = false;";
        Set<String> result = new LinkedHashSet<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                result.add(resultSet.getString(1));
            }
            return result;
        } catch (SQLException e) {
            logger.debug("Error in the method getDatabasesNames " + e);
            return result;
        }
    }
}