package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

import java.sql.*;

public class SqlGameDAO implements GameDAO{
    private final Gson gson = new Gson();

    public SqlGameDAO() {
        try { DatabaseManager.createDatabase(); } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
        try (var conn = DatabaseManager.getConnection()) {
            var createTestTable = """            
                    CREATE TABLE if NOT EXISTS game (
                                    gameID INT NOT NULL,
                                    whiteUsername VARCHAR(255),
                                    blackUsername VARCHAR(255),
                                    gameName VARCHAR(255),
                                    chessGame TEXT,
                                    PRIMARY KEY (gameID)
                                    )""";
            try (var createTableStatement = conn.prepareStatement(createTestTable)) {
                createTableStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameData> listGames(String username) throws DataAccessException {
        List<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM game")){
                try (var results = statement.executeQuery()) {
                    while (results.next()) {
                        var gameID = results.getInt("game_id");
                        var whiteUsername = results.getString("white_username");
                        var blackUsername = results.getString("black_username");
                        var gameName = results.getString("game_name");
                        var chessGame = deserializeChessGame(results.getString("chessGame"));
                        games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame));
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            return null;
        }
        return games;
    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, chessGame) VALUES(?, ?, ?, ?, ?)")) {
                statement.setInt(1, game.gameID());
                statement.setString(2, game.whiteUsername());
                statement.setString(3, game.blackUsername());
                statement.setString(4, game.gameName());
                statement.setString(5, serializeChessGame(game.game()));
                statement.executeUpdate();

            }
        } catch (SQLException e) {
            throw new DataAccessException("Error creating game: " + e.getMessage());
        }
        throw new DataAccessException("Failed to create game");
    }


    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT whiteUsername, blackUsername, gameName, chessGame FROM game WHERE gameID=?")){
                statement.setInt(1, gameID);
                try (var result = statement.executeQuery()) {
                    result.next();
                    var whiteUsername = result.getString("white_username");
                    var blackUsername = result.getString("black_username");
                    var gameName = result.getString("game_name");
                    var chessGame = deserializeChessGame(result.getString("chess_game"));
                    return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Game not found with id: " + gameID);
        }
    }

    @Override
    public void updateGame(GameData updatedGame) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("UPDATE game SET white_username=?, black_username=?, game_name=?, game_state=? WHERE game_id=?")) {
                statement.setString(1, updatedGame.whiteUsername());
                statement.setString(2, updatedGame.blackUsername());
                statement.setString(3, updatedGame.gameName());
                statement.setString(4, serializeChessGame(updatedGame.game()));
                statement.setInt(5, updatedGame.gameID());
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected == 0) {
                    throw new DataAccessException("No game found with ID: " + updatedGame.gameID());
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE game")) {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException | DataAccessException e) {
        }
    }

    private String serializeChessGame(ChessGame game) {
        return gson.toJson(game);
    }

    private ChessGame deserializeChessGame(String json) {
        return gson.fromJson(json, ChessGame.class);
    }

}
