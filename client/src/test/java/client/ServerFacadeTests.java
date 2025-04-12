package client;

import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import org.junit.jupiter.api.*;

import server.Server;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() throws Exception {
        server = new Server();
        var port = server.run(0);
        System.out.println(STR."Started test HTTP server on \{port}");
        facade = new ServerFacade(STR."localhost:\{port}");

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @AfterEach
    public void clear() {
        facade.setAuthToken(null);
        facade.http.clearDatabase();
    }

    private void registerAndLogin() {
        assertTrue(facade.register("testuser", "pass", "test@email.com"));
        assertTrue(facade.login("testuser", "pass"));
    }

    @Test
    public void testRegisterSuccess() {
        boolean success = facade.register("user1", "pass", "email@test.com");
        assertTrue(success);
    }

    @Test
    public void testRegisterFailure_Duplicate() {
        facade.register("user2", "pass", "email2@test.com");
        boolean result = facade.register("user2", "pass", "email2@test.com");
        assertFalse(result);
    }

    @Test
    public void testLoginSuccess() {
        facade.register("user3", "pass", "email3@test.com");
        assertTrue(facade.login("user3", "pass"));
    }

    @Test
    public void testLoginFailure_WrongPassword() {
        facade.register("user4", "pass", "email4@test.com");
        assertFalse(facade.login("user4", "wrongpass"));
    }

    @Test
    public void testLogoutSuccess() {
        registerAndLogin();
        assertTrue(facade.logout());
    }

    @Test
    public void testLogoutFailure_NotLoggedIn() {
        assertFalse(facade.logout());
    }

    @Test
    public void testCreateGameSuccess() {
        registerAndLogin();
        int id = facade.createGame("Test Game");
        assertTrue(id > 0);
    }

    @Test
    public void testCreateGameFailure_NoAuth() {
        int id = facade.createGame("Test Game");
        assertEquals(-1, id);
    }

    @Test
    public void testListGamesSuccess() {
        registerAndLogin();
        facade.createGame("Test Game");
        List<GameData> games = facade.listGames();
        assertFalse(games.isEmpty());
    }

    @Test
    public void testListGamesFailure_NotLoggedIn() {
        List<GameData> games = facade.listGames();
        assertTrue(games.isEmpty());
    }

    @Test
    public void testJoinGameSuccess() {
        registerAndLogin();
        int id = facade.createGame("Test Game");
        assertTrue(facade.joinGame(id, "white"));
    }

    @Test
    public void testJoinGameFailure_InvalidID() {
        registerAndLogin();
        assertFalse(facade.joinGame(-1, "white"));
    }

    @Test
    public void testMakeMoveSuccess() {
        registerAndLogin();
        int id = facade.createGame("Game");
        facade.joinGame(id, "white");

        ChessMove move = new ChessMove(new ChessPosition(2, 1), new ChessPosition(3, 1), null); // a2 to a3
        assertTrue(facade.makeMove(id, move));
    }

    @Test
    public void testMakeMoveFailure_InvalidGame() {
        registerAndLogin();
        ChessMove move = new ChessMove(new ChessPosition(2, 1), new ChessPosition(3, 1), null);
        assertFalse(facade.makeMove(-1, move));
    }

    @Test
    public void testResignGameSuccess() {
        registerAndLogin();
        int id = facade.createGame("Game");
        facade.joinGame(id, "white");
        assertTrue(facade.resignGame(id));
    }

    @Test
    public void testResignGameFailure_InvalidGame() {
        registerAndLogin();
        assertFalse(facade.resignGame(-1));
    }

    @Test
    public void testLeaveGameSuccess() {
        registerAndLogin();
        int id = facade.createGame("Game");
        facade.joinGame(id, "white");
        assertTrue(facade.leaveGame(id));
    }

    @Test
    public void testLeaveGameFailure_InvalidGame() {
        registerAndLogin();
        assertFalse(facade.leaveGame(-1));
    }
}