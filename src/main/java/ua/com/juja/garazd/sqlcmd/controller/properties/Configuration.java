package ua.com.juja.garazd.sqlcmd.controller.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class Configuration {

    public static final String CONFIG_SQLCMD_PROPERTIES = "src/main/resources/sqlcmd.properties";

    private Properties properties;

    public Configuration() {
        properties = new Properties();
        File file = new File(CONFIG_SQLCMD_PROPERTIES);
        try (FileInputStream fileInput = new FileInputStream(file)) {
            properties.load(fileInput);
        } catch (IOException e) {
            throw new RuntimeException("Loading error properties from file " + file.getAbsolutePath());
        }
    }

    public String getClassDriver() {
        return properties.getProperty("database.class.driver");
    }

    public String getJdbcDriver() {
        return properties.getProperty("database.jdbc.driver");
    }

    public String getServerName() {
        return properties.getProperty("database.server.name");
    }

    public String getPortNumber() {
        return properties.getProperty("database.port");
    }

    public String getDatabaseName() {
        return properties.getProperty("database.name");
    }

    public String getUserName() {
        return properties.getProperty("database.user.name");
    }

    public String getPassword() {
        return properties.getProperty("database.user.password");
    }
}