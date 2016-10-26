package ua.com.juja.garazd.sqlcmd.controller.properties;

import ua.com.juja.garazd.sqlcmd.model.DatabaseManager;

public class Support {

    private ConfigurationTest configurationTest = new ConfigurationTest();
    private String DATABASE_NAME = configurationTest.getDatabaseNameForTest();
    private String USER_NAME = configurationTest.getUserNameForTest();
    private String PASSWORD = configurationTest.getPasswordForTest();

    public void setupData(DatabaseManager manager) {
        try {
            manager.connectDatabase("", USER_NAME, PASSWORD);
        } catch (RuntimeException e) {
            throw new RuntimeException("For testing, change the name and password in a file config.properties."
                + "\n" + e.getCause());
        }
        manager.createDatabase(DATABASE_NAME);
        manager.connectDatabase(DATABASE_NAME, USER_NAME, PASSWORD);
        createTablesWithData(manager);
    }

    public void dropData(DatabaseManager manager) {
        try {
            manager.connectDatabase("", USER_NAME, PASSWORD);
            manager.dropDatabase(DATABASE_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createTablesWithData(DatabaseManager manager) {
        manager.createTable("users" +
            " (id SERIAL NOT NULL PRIMARY KEY, name VARCHAR (50) UNIQUE NOT NULL, password VARCHAR (50) NOT NULL)");
        manager.createTable("test (id SERIAL PRIMARY KEY)");
        manager.createTable("users2" +
            " (id SERIAL NOT NULL PRIMARY KEY, username varchar(225) NOT NULL UNIQUE, password varchar(225))");
    }
}