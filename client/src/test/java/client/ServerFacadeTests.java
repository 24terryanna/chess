package client;

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
        assertTrue(facade.createGame("Game 1"));
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
}
