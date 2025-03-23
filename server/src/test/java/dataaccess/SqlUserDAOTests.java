package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SqlUserDAOTests {
    UserDAO userDAO;
    UserData defaultUser;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        userDAO = new SqlUserDAO();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE user")) {
                statement.executeUpdate();
            }
        }

        defaultUser = new UserData("username", "password", "email");
    }

    @AfterEach
    void tearDown() throws SQLException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE user")) {
                statement.executeUpdate();
            }
        }
    }

    @Test
    void testCreateUser_Success() throws DataAccessException {
        assertDoesNotThrow(() -> userDAO.createUser(defaultUser));
        UserData retrievedUser = userDAO.getUser(defaultUser.username());
        assertNotNull(retrievedUser);
        assertEquals(defaultUser.username(), retrievedUser.username());
    }

    @Test
    void testCreateUser_Duplicate() throws DataAccessException {
        userDAO.createUser(defaultUser);
        DataAccessException thrown = assertThrows(DataAccessException.class, () -> userDAO.createUser(defaultUser));
        assertTrue(thrown.getMessage().contains("User already exists"));
    }

    @Test
    void testGetUser_Exists() throws DataAccessException {
        userDAO.createUser(defaultUser);
        UserData retrievedUser = userDAO.getUser(defaultUser.username());
        assertNotNull(retrievedUser);
        assertEquals(defaultUser.username(), retrievedUser.username());
    }

    @Test
    void testGetUser_NotExists() throws DataAccessException {
        UserData retrievedUser = userDAO.getUser("nonexistentUser");
        assertNull(retrievedUser);
    }

    @Test
    void testClearUsers() throws DataAccessException {
        userDAO.createUser(defaultUser);
        assertNotNull(userDAO.getUser(defaultUser.username()));

        userDAO.clear();
        assertNull(userDAO.getUser(defaultUser.username()));
    }
}
