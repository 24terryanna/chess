package dataaccess;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE game")) {
                statement.executeUpdate();
            }
        }

        ChessGame defaultChessGame = new ChessGame();
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        defaultChessGame.setBoard(board);
        defaultGameData = new GameData(1111, "white", "black", "gameName", defaultChessGame);
    }

    @AfterEach
    void tearDown() throws SQLException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE game")) {
                statement.executeUpdate();
            }
        }
    }

    @Test
    void testCreateGame() throws DataAccessException {
        gameDAO.createGame(defaultGameData);
        GameData retrievedGame = gameDAO.getGame(defaultGameData.gameID());

        assertNotNull(retrievedGame);
        assertEquals(defaultGameData.gameID(), retrievedGame.gameID());
        assertEquals(defaultGameData.whiteUsername(), retrievedGame.whiteUsername());
        assertEquals(defaultGameData.blackUsername(), retrievedGame.blackUsername());
        assertEquals(defaultGameData.gameName(), retrievedGame.gameName());
    }

    @Test
    void testGetGame_NotFound() {
        assertThrows(DataAccessException.class, () -> gameDAO.getGame(9999)); // Random non-existent ID
    }

    @Test
    void testUpdateGame() throws DataAccessException {
        gameDAO.createGame(defaultGameData);
        GameData updatedGame = new GameData(1111, "newWhite", "newBlack", "newGameName", defaultGameData.game());
        gameDAO.updateGame(updatedGame);

        GameData retrievedGame = gameDAO.getGame(1111);
        assertEquals("newWhite", retrievedGame.whiteUsername());
        assertEquals("newBlack", retrievedGame.blackUsername());
        assertEquals("newGameName", retrievedGame.gameName());
    }

    @Test
    void testListGames() throws DataAccessException {
        gameDAO.createGame(defaultGameData);
        List<GameData> games = gameDAO.listGames("white");

        assertEquals(1, games.size());
        assertEquals(defaultGameData.gameID(), games.get(0).gameID());
    }

    @Test
    void testClear() throws DataAccessException {
        gameDAO.createGame(defaultGameData);
        gameDAO.clear();

        assertThrows(DataAccessException.class, () -> gameDAO.getGame(defaultGameData.gameID()));
    }


}
