package client;

import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import org.junit.jupiter.api.*;

import server.Server;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


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


    @Test
    void registerSuccess() {
        assertTrue(facade.register("user1", "pass123", "user1@email.com"));
    }

    @Test
    void registerDuplicateFails() {
        facade.register("user2", "pass123", "user2@email.com");
        assertFalse(facade.register("user2", "pass123", "user2@email.com"));
    }

    @Test
    void loginSuccess() {
        facade.register("user3", "pass123", "user3@email.com");
        assertTrue(facade.login("user3", "pass123"));
    }

    @Test
    void loginFailsWrongPassword() {
        facade.register("user4", "pass123", "user4@email.com");
        assertFalse(facade.login("user4", "wrongpass"));
    }

    @Test
    void logoutSuccess() {
        facade.register("user5", "pass123", "user5@email.com");
        facade.login("user5", "pass123");
        assertTrue(facade.logout());
    }

    @Test
    void createGameSuccess() {
        facade.register("user6", "pass123", "user6@email.com");
        facade.login("user6", "pass123");

        int gameID = facade.createGame("Game 1");

        assertTrue(gameID != -1, "Expected valid game ID, but got -1 (failure).");
    }

    @Test
    void listGamesIncludesCreatedGame() {
        facade.register("user7", "pass123", "user7@email.com");
        facade.login("user7", "pass123");
        facade.createGame("Test Game");
        List<GameData> games = facade.listGames();
        assertTrue(games.stream().anyMatch(game -> game.gameName().equals("Test Game")));
    }

    @Test
    void joinGameSuccess() {
        facade.register("user8", "pass123", "user8@email.com");
        facade.login("user8", "pass123");
        facade.createGame("Join Game Test");
        List<GameData> games = facade.listGames();
        assertFalse(games.isEmpty());
        int gameId = games.get(0).gameID();
        assertTrue(facade.joinGame(gameId, "WHITE"));
    }

    @Test
    void makeMoveSuccess() {
        // Setup
        facade.register("user9", "pass123", "user9@email.com");
        facade.login("user9", "pass123");
        facade.createGame("Move Test");
        int gameId = facade.listGames().get(0).gameID();
        facade.joinGame(gameId, "WHITE");

        // Move: e2 to e4
        ChessMove move = new ChessMove(new ChessPosition(2, 5), new ChessPosition(4, 5), null); // e2 -> e4
        boolean success = facade.makeMove(gameId, move);

        assertTrue(success, "Move should be accepted");
    }

    @Test
    void resignGameSuccess() {
        facade.register("user10", "pass123", "user10@email.com");
        facade.login("user10", "pass123");
        facade.createGame("Resign Test");
        int gameId = facade.listGames().get(0).gameID();
        facade.joinGame(gameId, "BLACK");

        boolean success = facade.resignGame(gameId);
        assertTrue(success, "Should be able to resign from a game");
    }

}
