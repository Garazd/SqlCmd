package ua.com.juja.garazd.sqlcmd.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.juja.garazd.sqlcmd.controller.command.IsConnected;
import ua.com.juja.garazd.sqlcmd.controller.properties.Configuration;
import ua.com.juja.garazd.sqlcmd.view.View;

public class DatabaseConnectionImpl implements DatabaseConnection {

    private static Configuration configuration = new Configuration();
    private static String DATABASE_NAME = configuration.getDatabaseName();
    private static String USER_NAME = configuration.getUserName();
    private static String PASSWORD = configuration.getPassword();
    private static Logger logger = LogManager.getLogger(DatabaseManagerImpl.class.getName());
    private IsConnected isConnected;

    private Connection connection;
    private View view;

    static {
        try {
            Class.forName(configuration.getClassDriver());
        } catch (ClassNotFoundException e) {
            System.out.println("Please load database driver to project.");
            logger.debug("Error in the method connectDatabase " + e);
        }
    }

    public Connection getConnection() {
        try {
            Configuration configuration = new Configuration();
            String databaseUrl = String.format("%s%s:%s/%s",
                configuration.getJdbcDriver(),
                configuration.getServerName(),
                configuration.getPortNumber(),
                configuration.getDatabaseName());
            return connection = DriverManager.getConnection(databaseUrl, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            connection = null;
            logger.debug("Error in the method connectDatabase " + e);
            throw new RuntimeException("Unable to connectDatabase: " + DATABASE_NAME + " User name: " + USER_NAME, e);
        }
    }

    @Override
    public void connectDatabase(String databaseName, String userName, String password) {
        try {
            Configuration configuration = new Configuration();
            databaseName = configuration.getDatabaseName();
            userName = configuration.getUserName();
            password = configuration.getPassword();
            isConnected.canProcess("connect");
        } catch (Exception e) {
            connection = null;
            logger.debug("Error in the method connectionDatabase " + e);
            view.write("Please enter the correct values in the file configuration.");
        }

        if (!isConnected()) {
            view.write("To retry? (y/n):");
            String input = view.read();
            if (!input.equalsIgnoreCase("y")) {
                view.write("See you later! Bye");
            }
        }
        view.write("Connecting to a database is successful");
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }
}