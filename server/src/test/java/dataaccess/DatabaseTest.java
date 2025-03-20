package dataaccess;

import model.GameData;
import chess.ChessGame;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SqlGameDAOTest {
    private static SqlGameDAO gameDAO;

    @BeforeEach
    void setUp() throws DataAccessException {
        gameDAO = new SqlGameDAO();
        gameDAO.clear(); // Ensure a fresh start
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        gameDAO.clear(); // Clean up after tests
    }

    @Test
    void testListGamesPositive() throws DataAccessException {
        GameData game = new GameData(0, "Alice", "Bob", "TestGame", new ChessGame());
        gameDAO.createGame(game);

        List<GameData> games = gameDAO.listGames("Alice");
        assertFalse(games.isEmpty(), "List should not be empty after inserting a game");
    }

    @Test
    void testListGamesNegative() throws DataAccessException {
        List<GameData> games = gameDAO.listGames("NonExistentUser");
        assertTrue(games.isEmpty(), "List should be empty for a non-existent user");
    }

    @Test
    void testCreateGamePositive() throws DataAccessException {
        GameData game = new GameData(0, "Alice", "Bob", "ValidGame", new ChessGame());
        GameData createdGame = gameDAO.createGame(game);

        assertNotNull(createdGame, "Created game should not be null");
        assertTrue(createdGame.gameID() > 0, "Game ID should be assigned");
    }

    @Test
    void testCreateGameNegative() {
        GameData invalidGame = new GameData(0, "Alice", "Bob", null, new ChessGame());

        assertThrows(DataAccessException.class, () -> gameDAO.createGame(invalidGame),
                "Should throw an exception for an invalid game name");
    }

    @Test
    void testGetGamePositive() throws DataAccessException {
        GameData game = new GameData(0, "Alice", "Bob", "RetrieveTest", new ChessGame());
        GameData createdGame = gameDAO.createGame(game);

        GameData retrievedGame = gameDAO.getGame(createdGame.gameID());
        assertNotNull(retrievedGame, "Retrieved game should not be null");
        assertEquals("RetrieveTest", retrievedGame.gameName(), "Game name should match");
    }

    @Test
    void testGetGameNegative() throws DataAccessException {
        GameData retrievedGame = gameDAO.getGame(9999); // ID that doesn't exist
        assertNull(retrievedGame, "Retrieving a non-existent game should return null");
    }

    @Test
    void testUpdateGamePositive() throws DataAccessException {
        GameData game = new GameData(0, "Alice", "Bob", "UpdateTest", new ChessGame());
        GameData createdGame = gameDAO.createGame(game);

        ChessGame newGameState = new ChessGame(); // Assume this represents a new state
        GameData updatedGame = new GameData(createdGame.gameID(), "Alice", "Bob", "UpdateTest", newGameState);
        gameDAO.updateGame(updatedGame);

        GameData retrievedGame = gameDAO.getGame(createdGame.gameID());
        assertEquals("UpdateTest", retrievedGame.gameName(), "Game name should remain unchanged");
        assertNotNull(retrievedGame.game(), "Game state should be updated");
    }

    @Test
    void testUpdateGameNegative() {
        GameData nonExistentGame = new GameData(9999, "Alice", "Bob", "FakeGame", new ChessGame());

        assertThrows(DataAccessException.class, () -> gameDAO.updateGame(nonExistentGame),
                "Updating a non-existent game should throw an exception");
    }

    @Test
    void testClearPositive() throws DataAccessException {
        GameData game = new GameData(0, "Alice", "Bob", "ClearTest", new ChessGame());
        gameDAO.createGame(game);

        gameDAO.clear();
        List<GameData> games = gameDAO.listGames("Alice");
        assertTrue(games.isEmpty(), "Database should be empty after clearing");
    }
}
