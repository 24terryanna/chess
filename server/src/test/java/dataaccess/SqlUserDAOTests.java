package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SqlUserDAOTests {
    UserDAO userDAO;
    UserData defaultUser;

//
    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        userDAO = new SqlUserDAO();
        try (var conn = DatabaseManager.getConnection()) {
            try (var psDisableFK = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0")) {
                psDisableFK.executeUpdate();
            }
            try (var psDeleteAuth = conn.prepareStatement("DELETE FROM auth")) {
                psDeleteAuth.executeUpdate();
            }
            try (var psDeleteGame = conn.prepareStatement("DELETE FROM game")) {
                psDeleteGame.executeUpdate();
            }
            try (var psDeleteUser = conn.prepareStatement("DELETE FROM user")) {
                psDeleteUser.executeUpdate();
            }
            try (var psEnableFK = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1")) {
                psEnableFK.executeUpdate();
            }
        }
        defaultUser = new UserData("username", "password", "email");
    }


    @AfterEach
    void tearDown() throws SQLException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var psDisableFK = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0")) {
                psDisableFK.executeUpdate();
            }
            try (var psDeleteAuth = conn.prepareStatement("DELETE FROM auth")) {
                psDeleteAuth.executeUpdate();
            }
            try (var psDeleteGame = conn.prepareStatement("DELETE FROM game")) {
                psDeleteGame.executeUpdate();
            }
            try (var psDeleteUser = conn.prepareStatement("DELETE FROM user")) {
                psDeleteUser.executeUpdate();
            }
            try (var psEnableFK = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1")) {
                psEnableFK.executeUpdate();
            }
        }
    }


    @Test
    void testCreateUserSuccess() throws DataAccessException {
        assertDoesNotThrow(() -> userDAO.createUser(defaultUser));
        UserData retrievedUser = userDAO.getUser(defaultUser.username());
        assertNotNull(retrievedUser);
        assertEquals(defaultUser.username(), retrievedUser.username());
    }

    @Test
    void testCreateUserDuplicate() throws DataAccessException {
        userDAO.createUser(defaultUser);
        DataAccessException thrown = assertThrows(DataAccessException.class, () -> userDAO.createUser(defaultUser));
        assertTrue(thrown.getMessage().contains("User already exists"));
    }

    @Test
    void testGetUserExists() throws DataAccessException {
        userDAO.createUser(defaultUser);
        UserData retrievedUser = userDAO.getUser(defaultUser.username());
        assertNotNull(retrievedUser);
        assertEquals(defaultUser.username(), retrievedUser.username());
    }

    @Test
    void testGetUserNotExists() throws DataAccessException {
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
