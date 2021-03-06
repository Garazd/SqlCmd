package ua.com.juja.garazd.sqlcmd.controller.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationTest {

    public static final String CONFIG_SQLCMD_PROPERTIES = "src/main/resources/sqlcmd.properties";

    private Properties properties;

    public ConfigurationTest() {
        properties = new Properties();
        File file = new File(CONFIG_SQLCMD_PROPERTIES);
        try (FileInputStream fileInput = new FileInputStream(file)) {
            properties.load(fileInput);
        } catch (IOException e) {
            throw new RuntimeException("Loading error properties from file " + file.getAbsolutePath());
        }
    }

    public String getDatabaseNameForTest() {
        return properties.getProperty("database.test.name");
    }

    public String getUserNameForTest() {
        return properties.getProperty("database.test.user.name");
    }

    public String getPasswordForTest() {
        return properties.getProperty("database.test.user.password");
    }
}