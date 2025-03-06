package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import model.GamesList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
