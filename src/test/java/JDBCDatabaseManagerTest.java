import model.DatabaseManager;

public class JDBCDatabaseManagerTest extends DatabaseManagerTest {
    @Override
    public DatabaseManager getDatabaseManager() {
        return new JDBCDatabaseManagerTest();
    }
}