package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SqlAuthDAOTests {
    AuthDAO authDAO;
    AuthData defaultAuth;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        authDAO = new SqlAuthDAO();
        clearDatabase();
        defaultAuth = new AuthData("token", "username");
    }

    @AfterEach
    void tearDown() throws SQLException, DataAccessException {
        clearDatabase();
    }

    private void clearDatabase() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE auth")) {
                statement.executeUpdate();
            }
        }
        authDAO.clear();
    }

    @Test
    void createAuthPositive() throws DataAccessException {
        // Positive test: Creating a valid auth record
        authDAO.createAuth(defaultAuth);
        AuthData retrievedAuth = authDAO.getAuth(defaultAuth.authToken());
        assertNotNull(retrievedAuth);
        assertEquals(defaultAuth.authToken(), retrievedAuth.authToken());
        assertEquals(defaultAuth.username(), retrievedAuth.username());
    }

    @Test
    void createAuthNegDuplicateAuthToken() {
        // Negative test: Attempting to create a record with a duplicate auth token
        assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth(defaultAuth);
            authDAO.createAuth(defaultAuth); // Duplicate auth token
        });
    }

    @Test
    void getAuthPositive() throws DataAccessException {
        // Positive test: Retrieving an existing auth record
        authDAO.createAuth(defaultAuth);
        AuthData retrievedAuth = authDAO.getAuth(defaultAuth.authToken());
        assertNotNull(retrievedAuth);
        assertEquals(defaultAuth.authToken(), retrievedAuth.authToken());
        assertEquals(defaultAuth.username(), retrievedAuth.username());
    }

    @Test
    void getAuthNegNotAuthToken() throws DataAccessException {
        // Negative test: Attempting to retrieve a nonexistent auth token
        AuthData retrievedAuth = authDAO.getAuth("nonexistent_token");
        assertNull(retrievedAuth);
    }

    @Test
    void deleteAuthPositive() throws DataAccessException {
        // Positive test: Deleting an existing auth record
        authDAO.createAuth(defaultAuth);
        authDAO.deleteAuth(defaultAuth.authToken());
        AuthData retrievedAuth = authDAO.getAuth(defaultAuth.authToken());
        assertNull(retrievedAuth); // Auth record should no longer exist
    }

    @Test
    void deleteAuthNegNoMatchAuthToken() {
        // Negative test: Attempting to delete a nonexistent auth token
        assertDoesNotThrow(() -> authDAO.deleteAuth("nonexistent_token"));
    }

    @Test
    void clearPositive() throws DataAccessException {
        // Positive test: Clearing the auth table
        authDAO.createAuth(defaultAuth);
        authDAO.clear();
        AuthData retrievedAuth = authDAO.getAuth(defaultAuth.authToken());
        assertNull(retrievedAuth); // Table should be empty
    }

}
