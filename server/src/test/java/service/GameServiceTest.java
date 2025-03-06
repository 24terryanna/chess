package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import model.GamesList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private GameService gameService;
    private MemoryGameDAO gameDAO;
    private MemoryAuthDAO authDAO;

    @BeforeEach
    void setUp() {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        gameService = new GameService(gameDAO, authDAO);
    }

    // List Games: Successful case
    @Test
    void testListGamesSuccess() throws DataAccessException {
        AuthData authData = new AuthData("validToken", "validUser");
        authDAO.createAuth(authData);

        GamesList result = gameService.listGames("validToken");

        assertEquals(200, result.statusCode());
        assertNotNull(result.games());
    }

    // List Games: Unauthorized
    @Test
    void testListGamesUnauthorized() throws DataAccessException {
        GamesList result = gameService.listGames("invalidToken");

        assertEquals(401, result.statusCode());
        assertEquals("Error: unauthorized", result.message());
    }

    // Create Game: Successful case
    @Test
    void testCreateGameSuccess() throws DataAccessException {
        authDAO.createAuth(new AuthData("validToken", "validUser"));
        int gameID = gameService.createGame("validToken", "New Game");

        assertTrue(gameID > 0);
        assertNotNull(gameDAO.getGame(gameID));
    }

    // Create Game: Unauthorized
    @Test
    void testCreateGameUnauthorized() {
        assertThrows(DataAccessException.class, () -> gameService.createGame("invalidToken", "New Game"));
    }

    // Join Game: Successful case
    @Test
    void testJoinGameSuccess() throws DataAccessException {
        authDAO.createAuth(new AuthData("validToken", "player1"));
        int gameID = gameService.createGame("validToken", "Chess Match");

        gameService.joinGame("validToken", "WHITE", gameID);
        GameData updatedGame = gameDAO.getGame(gameID);

        assertEquals("player1", updatedGame.whiteUsername());
    }

    // Join Game: Spot already taken
    @Test
    void testJoinGameSpotTaken() throws DataAccessException {
        authDAO.createAuth(new AuthData("player1Token", "player1"));
        authDAO.createAuth(new AuthData("player2Token", "player2"));

        int gameID = gameService.createGame("player1Token", "Chess Match");
        gameService.joinGame("player1Token", "WHITE", gameID);

        assertThrows(DataAccessException.class, () -> gameService.joinGame("player2Token", "WHITE", gameID));
    }
    // MemoryGameDAO Tests
    @Test
    void testListGamesPositive() throws DataAccessException {
        gameDAO.createGame(new GameData(0, null, null, "Chess1", new ChessGame()));
        List<GameData> games = gameDAO.listGames("anyUser");
        assertFalse(games.isEmpty());
    }

    @Test
    void testListGamesNegative() throws DataAccessException {
        List<GameData> games = gameDAO.listGames("anyUser");
        assertTrue(games.isEmpty());
    }

    @Test
    void testCreateGamePositive() throws DataAccessException {
        GameData game = new GameData(0, null, null, "Chess1", new ChessGame());
        assertDoesNotThrow(() -> gameDAO.createGame(game));
    }

    @Test
    void testCreateGameNegative() {
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @Test
    void testGetGamePositive() throws DataAccessException {
        GameData game = gameDAO.createGame(new GameData(0, null, null, "Chess1", new ChessGame()));
        assertEquals(game, gameDAO.getGame(game.gameID()));
    }

    @Test
    void testGetGameNegative() {
        assertThrows(DataAccessException.class, () -> gameDAO.getGame(999));
    }

    @Test
    void testUpdateGamePositive() throws DataAccessException {
        GameData game = gameDAO.createGame(new GameData(0, null, null, "Chess1", new ChessGame()));
        GameData updatedGame = new GameData(game.gameID(), "player1", null, "UpdatedChess", new ChessGame());
        assertDoesNotThrow(() -> gameDAO.updateGame(updatedGame));
    }

    @Test
    void testUpdateGameNegative() {
        GameData game = new GameData(999, "player1", null, "NonexistentGame", new ChessGame());
        assertThrows(DataAccessException.class, () -> gameDAO.updateGame(game));
    }

    @Test
    void testClearGames() throws DataAccessException {
        gameDAO.createGame(new GameData(0, null, null, "Chess1", new ChessGame()));
        gameDAO.clear();
        assertTrue(gameDAO.listGames("anyUser").isEmpty());
    }
}

