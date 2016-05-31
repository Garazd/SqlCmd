import model.DatabaseManager;

public class InMemoryDatabaseManagerTest extends DatabaseManagerTest {
    @Override
    public DatabaseManager getDatabaseManager() {
        return new InMemoryDatabaseManagerTest();
    }
}