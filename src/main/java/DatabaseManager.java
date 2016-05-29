import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class DatabaseManager {

    private Connection connection;

    public void create(DataSet input) {
        Statement statement = null;
        try {
            statement = connection.createStatement();

            String tableNames = getNameFormated(input, "%s,");
            String values = getValueFormated(input, "%s,");

            statement.executeUpdate("INSERT INTO public.user (" + tableNames + ")" +
                "VALUES (" + values + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(String tableName, int id, DataSet newValue) {
        PreparedStatement preparedStatement = null;
        try {
            String tableNames = getNameFormated(newValue, "%s,");

            String sql = "UPDATE public." + tableName + " SET " + tableNames +
                " WHERE id = ?";
            System.out.println(sql);
            preparedStatement = connection.prepareStatement(sql);
            int index = 1;
            for (Object value : newValue.getValue()) {
                preparedStatement.setObject(index , value);
                index++;
            }
            preparedStatement.setObject(index, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void clear(String tableName) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM public." + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] getTableName() {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT table_name FROM information_schema.tables " +
                "WHERE table_schema='public' AND table_type='base table'");
            String[] tables = new String[100];
            int index = 0;
            while (resultSet.next()) {
                tables[index++] = resultSet.getString("table_name");
            }
            tables = Arrays.copyOf(tables, index, String[].class);
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public DataSet[] getTableData(String tableName) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            int size = getSize(tableName);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM public." + tableName);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            DataSet[] result = new DataSet[size];
            int index = 0;
            while (resultSet.next()) {
                DataSet dataSet = new DataSet();
                result[index++] = dataSet;
                for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                    dataSet.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                }
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new DataSet[0];
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void connect(String database, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Please add JDBC jar to project.");
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(
                "jdbs:postgresql://localhost:5432/" + database, user, password);
        } catch (SQLException e) {
            System.out.println(String.format("Cant get connection for database:%s user:%s", database, user));
            e.printStackTrace();
            connection = null;
        }
    }

    private static String getNameFormated(DataSet newValue, String format) {
        String string = "";
        for (String name : newValue.getName()) {
            string += String.format(format, name);
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }

    private String getValueFormated(DataSet input, String format) {
        String values = "";
        for (Object value : input.getValue()) {
            values += String.format(format, value);
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

    private int getSize(String tableName) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM public." + tableName);
        resultSet.next();
        int size = resultSet.getInt(1);
        resultSet.close();
        return size;
    }
}