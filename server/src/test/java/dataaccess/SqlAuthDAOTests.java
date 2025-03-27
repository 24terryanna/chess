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
    void createAuthNegDuplicateAuthToken() {
        assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth(defaultAuth);
            authDAO.createAuth(defaultAuth);
        });
    }

    @Test
    void createAuthPositive() throws DataAccessException {
        verifyAuthCreateRetrieve();
    }

    @Test
    void getAuthPositive() throws DataAccessException {
        verifyAuthCreateRetrieve();
    }

    private void verifyAuthCreateRetrieve() throws DataAccessException {
        authDAO.createAuth(defaultAuth);
        AuthData retrievedAuth = authDAO.getAuth(defaultAuth.authToken());
        assertNotNull(retrievedAuth);
        assertEquals(defaultAuth.authToken(), retrievedAuth.authToken());
        assertEquals(defaultAuth.username(), retrievedAuth.username());
    }

    @Test
    void getAuthNegNotAuthToken() throws DataAccessException {
        AuthData retrievedAuth = authDAO.getAuth("nonexistent_token");
        assertNull(retrievedAuth);
    }

    @Test
    void deleteAuthPositive() throws DataAccessException {
        authDAO.createAuth(defaultAuth);
        authDAO.deleteAuth(defaultAuth.authToken());
        AuthData retrievedAuth = authDAO.getAuth(defaultAuth.authToken());
        assertNull(retrievedAuth);
    }

    @Test
    void deleteAuthNegNoMatchAuthToken() {
        assertDoesNotThrow(() -> authDAO.deleteAuth("nonexistent_token"));
    }

    @Test
    void clearPositive() throws DataAccessException {
        authDAO.createAuth(defaultAuth);
        authDAO.clear();
        AuthData retrievedAuth = authDAO.getAuth(defaultAuth.authToken());
        assertNull(retrievedAuth);
    }

}
