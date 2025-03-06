package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.req_res.*;
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
    void testRegister_Success() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("newUser", "password123", "user@example.com");
        RegisterResult result = userService.register(request);

        assertEquals(200, result.statusCode());
        assertEquals("newUser", result.username());
        assertNotNull(result.authToken());
        assertNotNull(authDAO.getAuth(result.authToken()));
    }

    // Register: Username already taken
    @Test
    void testRegister_UsernameTaken() throws DataAccessException {
        RegisterRequest request1 = new RegisterRequest("existingUser", "password123", "user@example.com");
        userService.register(request1); // First registration succeeds

        RegisterRequest request2 = new RegisterRequest("existingUser", "password123", "another@example.com");
        RegisterResult result = userService.register(request2);

        assertEquals(403, result.statusCode());
        assertEquals("Error: already taken", result.message());
    }

    // Login: Successful case
    @Test
    void testLogin_Success() throws DataAccessException {
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
    void testLogin_WrongPassword() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("validUser", "password123", "user@example.com");
        userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("validUser", "wrongPassword");
        LoginResult result = userService.login(loginRequest);

        assertEquals(401, result.statusCode());
        assertEquals("Error: unauthorized", result.message());
    }

    // Logout: Successful case
    @Test
    void testLogout_Success() throws DataAccessException {
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
    void testLogout_InvalidToken() throws DataAccessException {
        LogoutResult result = userService.logout("invalidToken");

        assertEquals(401, result.statusCode());
        assertEquals("Error: unauthorized", result.message());
    }
}
