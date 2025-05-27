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
                        game_id INT NOT NULL AUTO_INCREMENT,
                        white_username VARCHAR(255),
                        black_username VARCHAR(255),
                        game_name VARCHAR(255),
                        chess_game TEXT,
                        PRIMARY KEY (game_id)
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
            try (var statement = conn.prepareStatement("SELECT game_id, white_username, black_username, game_name, chess_game FROM game")){
                try (var results = statement.executeQuery()) {
                    while (results.next()) {
                        var gameID = results.getInt("game_id");
                        var whiteUsername = results.getString("white_username");
                        var blackUsername = results.getString("black_username");
                        var gameName = results.getString("game_name");
                        var chessGame = deserializeChessGame(results.getString("chess_game"));
                        games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame));
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException("Error retrieving games: " + e.getMessage());
        }
        return games;
    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        if (game == null || game.gameName() == null || game.game() == null) {
            throw new DataAccessException("Invalid game data");
        }

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(
                    "INSERT INTO game (white_username, black_username, game_name, chess_game) VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, game.whiteUsername());
                statement.setString(2, game.blackUsername());
                statement.setString(3, game.gameName());
                statement.setString(4, serializeChessGame(game.game()));

                statement.executeUpdate();

                //generate game_id
                try (var resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        int generatedID = resultSet.getInt(1);
                        return new GameData(generatedID, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
                    } else {
                        throw new DataAccessException("Failed to retrieve generated game ID.");
                    }
                }

            }
        } catch (SQLException e) {
            throw new DataAccessException("Error creating game: " + e.getMessage());
        }

    }


    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(
                    "SELECT white_username, black_username, game_name, chess_game FROM game WHERE game_id=?")){
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
        if (updatedGame == null || updatedGame.gameName() == null || updatedGame.game() == null) {

        }

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(
                    "UPDATE game SET white_username=?, black_username=?, game_name=?, chess_game=? WHERE game_id=?")) {
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
            try (var statement = conn.prepareStatement("DELETE FROM game")) {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error clearing game table: " + e.getMessage());
        }
    }

    private String serializeChessGame(ChessGame game) {
        return gson.toJson(game);
    }

    private ChessGame deserializeChessGame(String json) {
        return gson.fromJson(json, ChessGame.class);
    }

}
