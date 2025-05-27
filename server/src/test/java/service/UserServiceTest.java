package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import model.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService userService;
    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;

    @BeforeEach
    void setUp() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    // Register: Successful case
    @Test
    void testRegisterSuccess() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("newUser", "password123", "user@example.com");
        RegisterResult result = userService.register(request);

        assertEquals(200, result.statusCode());
        assertEquals("newUser", result.username());
        assertNotNull(result.authToken());
        assertNotNull(authDAO.getAuth(result.authToken()));
    }

    // Register: Username already taken
    @Test
    void testRegisterUsernameTaken() throws DataAccessException {
        RegisterRequest request1 = new RegisterRequest("existingUser", "password123", "user@example.com");
        userService.register(request1); // First registration succeeds

        RegisterRequest request2 = new RegisterRequest("existingUser", "password123", "another@example.com");
        RegisterResult result = userService.register(request2);

        assertEquals(403, result.statusCode());
        assertEquals("Error: already taken", result.message());
    }

    // Login: Successful case
    @Test
    void testLoginSuccess() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("validUser", "password123", "user@example.com");
        userService.register(registerRequest); // Ensure user exists

        LoginRequest loginRequest = new LoginRequest("validUser", "password123");
        LoginResult result = userService.login(loginRequest);

        assertEquals(200, result.statusCode());
        assertEquals("validUser", result.username());
        assertNotNull(result.authToken());
        assertNotNull(authDAO.getAuth(result.authToken()));
    }

    // Login: Wrong password
    @Test
    void testLoginWrongPassword() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("validUser", "password123", "user@example.com");
        userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("validUser", "wrongPassword");
        LoginResult result = userService.login(loginRequest);

        assertEquals(401, result.statusCode());
        assertEquals("Error: unauthorized", result.message());
    }

    // Logout: Successful case
    @Test
    void testLogoutSuccess() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("validUser", "password123", "user@example.com");
        RegisterResult registerResult = userService.register(registerRequest);

        String authToken = registerResult.authToken();
        LogoutResult result = userService.logout(authToken);

        assertEquals(200, result.statusCode());
        assertEquals("Successful logout", result.message());
        assertNull(authDAO.getAuth(authToken)); // Token should be removed
    }

    // Logout: Invalid token
    @Test
    void testLogoutInvalidToken() throws DataAccessException {
        LogoutResult result = userService.logout("invalidToken");

        assertEquals(401, result.statusCode());
        assertEquals("Error: unauthorized", result.message());
    }

    // MemoryUserDAO Tests
    @Test
    void testGetUserPositive() throws DataAccessException {
        UserData user = new UserData("testUser", "password", "email@test.com");
        userDAO.createUser(user);
        assertEquals(user, userDAO.getUser("testUser"));
    }

    @Test
    void testGetUserNegative() throws DataAccessException {
        assertNull(userDAO.getUser("nonExistentUser"));
    }

    @Test
    void testCreateUserPositive() throws DataAccessException {
        UserData user = new UserData("newUser", "password", "email@domain.com");
        assertDoesNotThrow(() -> userDAO.createUser(user));
        assertEquals(user, userDAO.getUser("newUser"));
    }

    @Test
    void testCreateUserNegative() throws DataAccessException {
        UserData user = new UserData("duplicateUser", "password", "email@domain.com");
        userDAO.createUser(user);
        assertThrows(RuntimeException.class, () -> userDAO.createUser(user));
    }

    @Test
    void testClearUsers() throws DataAccessException {
        userDAO.createUser(new UserData("testUser", "pass", "email"));
        userDAO.clear();
        assertNull(userDAO.getUser("testUser"));
    }
}
