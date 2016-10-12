package ua.com.juja.garazd.sqlcmd.model;

import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.juja.garazd.sqlcmd.controller.properties.Configuration;

public class DatabaseConnectionImpl implements DatabaseConnection {

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
            logger.debug("Error in the method connectDatabase " + e);
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
            logger.debug("Error in the method connectionDatabase " + e);
            throw new RuntimeException("Please enter the correct values in the file configuration.");
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }
}