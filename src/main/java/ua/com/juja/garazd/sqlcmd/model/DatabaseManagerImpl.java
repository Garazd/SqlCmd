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
import java.util.StringJoiner;
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
            throw new DatabaseManagerException("Error in the load load database driver to project ", e);
        }
    }

    @Override
    public void connectDatabase(String databaseName, String userName, String password) {
        try {
            String databaseUrl = String.format("%s%s:%s/%s",
                configuration.getJdbcDriver(),
                configuration.getServerName(),
                configuration.getPortNumber(),
                configuration.getDatabaseName());
            connection = DriverManager.getConnection(databaseUrl, USER_NAME, PASSWORD);
        } catch (Exception e) {
            connection = null;
            logger.debug("Error in the method connectionDatabase do not correct values in the file configuration " + e);
            throw new DatabaseManagerException("Please enter the correct values in the file configuration.", e);
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
            throw new DatabaseManagerException("Error in the method createDatabase ", e);
        }
    }

    @Override
    public void dropDatabase(String databaseName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP DATABASE " + databaseName);
        } catch (SQLException e) {
            logger.debug("Error in the method dropDatabase " + e);
            throw new DatabaseManagerException("Error in the method dropDatabase ", e);
        }
    }

    private void executeUpdate(String sql) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.debug("Error in the method executeUpdate " + e);
            throw new DatabaseManagerException("Error in the method executeUpdate ", e);
        }
    }

    @Override
    public void createTable(String query) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + query);
        } catch (SQLException e) {
            logger.debug("Error in the method createTable " + e);
            throw new DatabaseManagerException("Error in the method createTable ", e);
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
        StringJoiner tableNames = new StringJoiner(",");
        StringJoiner values = new StringJoiner("','", "'", "'");

        try (Statement statement = connection.createStatement()) {
            for (Map.Entry<String, Object> entry : input.entrySet()) {
                tableNames.add(entry.getKey());
                values.add(entry.getValue().toString());
            }
            statement.executeUpdate(String.format("INSERT INTO public.%s (%s)VALUES (%s)", tableName, tableNames, values));
        } catch (SQLException e) {
            logger.debug("Error in the method createEntry " + e);
            throw new DatabaseManagerException("Error in the method createEntry ", e);
        }
    }


    @Override
    public void updateTable(String tableName, int id, Map<String, Object> newValue) {
        StringJoiner tableNames = new StringJoiner(" = ?,", "", " = ?");
        newValue.entrySet().forEach(x -> tableNames.add(x.getKey()));

        String sqlUpdate = String.format("UPDATE public.%s SET %s WHERE id = ?", tableName, tableNames);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {
            int index = 1;
            for (Object value : newValue.values()) {
                preparedStatement.setObject(index, value);
                index++;
            }
            preparedStatement.setInt(index, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.debug("Error in the method updateTable " + e);
            throw new DatabaseManagerException("Error in the method updateTable ", e);
        }
    }

    @Override
    public void clearTable(String tableName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM public." + tableName);
        } catch (SQLException e) {
            logger.debug("Error in the method clearTable " + e);
            throw new DatabaseManagerException("Error in the method clearTable ", e);
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
            throw new DatabaseManagerException("Error in the method getTableNames ", e);
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
            throw new DatabaseManagerException("Error in the method getTableData ", e);
        }
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
            throw new DatabaseManagerException("Error in the method getTableColumns ", e);
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
            throw new DatabaseManagerException("Error in the method getDatabasesNames ", e);
        }
    }

    @Override
    public int getTableSize(String tableName) {
        try (Statement statement = connection.createStatement();
             ResultSet tableSize = statement.executeQuery("SELECT COUNT(*) FROM public." + tableName)) {
            tableSize.next();
            return tableSize.getInt(1);
        } catch (SQLException e) {
            logger.debug("Error in the method getDatabasesNames " + e);
            throw new DatabaseManagerException("Error in the method getDatabasesNames ", e);
        }
    }
}