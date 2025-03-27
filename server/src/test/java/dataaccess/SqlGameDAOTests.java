package dataaccess;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.List;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SqlGameDAOTests {

    GameDAO gameDAO;
    GameData defaultGameData;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        gameDAO = new SqlGameDAO();
        clearDatabase();

        ChessGame defaultChessGame = new ChessGame();
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        defaultChessGame.setBoard(board);
        defaultGameData = new GameData(1234, "white", "black", "gameName", defaultChessGame);
    }

    @AfterEach
    void tearDown() throws SQLException, DataAccessException {
        clearDatabase();
    }

    private void clearDatabase() throws SQLException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("DELETE FROM game")) {
                statement.executeUpdate();
            }
            try (var resetAutoIncrement = conn.prepareStatement("ALTER TABLE game AUTO_INCREMENT = 1")) {
                resetAutoIncrement.executeUpdate();
            }
        }
    }

    @Test
    void testCreateGame() throws DataAccessException {
        GameData createdGame = gameDAO.createGame(defaultGameData);
        GameData retrievedGame = gameDAO.getGame(createdGame.gameID());

        assertNotNull(retrievedGame);
        assertEquals(createdGame.gameID(), retrievedGame.gameID());
        assertEquals(defaultGameData.whiteUsername(), retrievedGame.whiteUsername());
        assertEquals(defaultGameData.blackUsername(), retrievedGame.blackUsername());
        assertEquals(defaultGameData.gameName(), retrievedGame.gameName());
    }


    @Test
    void testGetGameNotFound() {
        assertThrows(DataAccessException.class, () -> gameDAO.getGame(9999)); // Random non-existent ID
    }

    @Test
    void testUpdateGame() throws DataAccessException {
        GameData createdGame = gameDAO.createGame(defaultGameData);
        GameData updatedGame = new GameData(createdGame.gameID(), "newWhite", "newBlack", "newGameName", createdGame.game());

        gameDAO.updateGame((updatedGame));
        GameData retrievedGame = gameDAO.getGame(createdGame.gameID());

        assertEquals("newWhite", retrievedGame.whiteUsername());
        assertEquals("newBlack", retrievedGame.blackUsername());
        assertEquals("newGameName", retrievedGame.gameName());
    }

    @Test
    void testUpdateGameNonExistentGame() {
        GameData nonExistentGame = new GameData(9999, "newWhite", "newBlack", "newGameName", defaultGameData.game());
        assertThrows(DataAccessException.class, () -> gameDAO.updateGame(nonExistentGame)); // Attempt to update a non-existent game
    }

    @Test
    void testListGames() throws DataAccessException {
        GameData createdGame = gameDAO.createGame(defaultGameData);
        GameData retrievedGame = gameDAO.getGame(createdGame.gameID());

        assertNotNull(retrievedGame);
        List<GameData> games = gameDAO.listGames("white");

        assertEquals(1, games.size());
        assertEquals(createdGame.gameID(), games.get(0).gameID());
    }

    @Test
    void testListGamesNoGames() throws DataAccessException {
        List<GameData> games = gameDAO.listGames("nonExistentUser");
        assertTrue(games.isEmpty(), "Expected no games for non-existent user");
    }

    @Test
    void testClear() throws DataAccessException {
        GameData createdGame = gameDAO.createGame(defaultGameData);
        int gameID = createdGame.gameID(); // 🔹 Store the real game ID
        gameDAO.clear();

        assertThrows(DataAccessException.class, () -> gameDAO.getGame(gameID));
    }

    @Test
    void testClearAndCreate() throws DataAccessException {
        gameDAO.createGame(defaultGameData);
        gameDAO.clear();

        // Verify the table is empty
        List<GameData> games = gameDAO.listGames("white");
        assertTrue(games.isEmpty(), "Expected no games after clear");

        // Verify a new game can be created after clearing
        GameData createdGame = gameDAO.createGame(defaultGameData);
        GameData retrievedGame = gameDAO.getGame(createdGame.gameID());

        assertNotNull(retrievedGame);
    }

}
