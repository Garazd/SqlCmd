package ua.com.juja.garazd.sqlcmd.controller.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class Configuration {

    public static final String CONFIG_SQLCMD_PROPERTIES = "src/main/resources/sqlcmd.properties";

    private Properties properties;
    private String databaseName;
    private String serverName;
    private String portNumber;
    private String userName;
    private String classDriver;
    private String jdbcDriver;
    private String password;

    public Configuration() {
        loadProperties();
    }

    public void loadProperties() {
        properties = new Properties();
        File file = new File(CONFIG_SQLCMD_PROPERTIES);
        try (FileInputStream fileInput = new FileInputStream(file)) {
            properties.load(fileInput);
            classDriver = properties.getProperty("database.class.driver");
            jdbcDriver = properties.getProperty("database.jdbc.driver");
            serverName = properties.getProperty("database.server.name");
            databaseName = properties.getProperty("database.name");
            portNumber = properties.getProperty("database.port");
            userName = properties.getProperty("database.user.name");
            password = properties.getProperty("database.user.password");
        } catch (IOException e) {
            throw new RuntimeException("Loading error properties from file " + file.getAbsolutePath());
        }
    }

    public String getServerName() {
        return serverName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getClassDriver() {
        return classDriver;
    }
}