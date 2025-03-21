package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

import java.sql.*;

public class SqlGameDAO implements GameDAO{
    private final Gson gson = new Gson();

    public SqlGameDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public List<GameData> listGames(String username) throws DataAccessException {
        List<GameData> games = new ArrayList<>();
        var statement = "SELECT * FROM game";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                games.add(new GameData(
                        rs.getInt("game_id"),
                        rs.getString("white_username"),
                        rs.getString("black_username"),
                        rs.getString("game_name"),
                        deserializeChessGame(rs.getString("game_state"))
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing games: " + e.getMessage());
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
    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            var createUserTable = """
                CREATE TABLE IF NOT EXISTS user (
                username VARCHAR(255) PRIMARY KEY,
                password VARCHAR(255) NOT NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """;

            var createGameTable = """
            CREATE TABLE IF NOT EXISTS game (
                game_id INT AUTO_INCREMENT PRIMARY KEY,
                white_username VARCHAR(255),
                black_username VARCHAR(255),
                game_name VARCHAR(255) NOT NULL,
                game_state TEXT NOT NULL,
                FOREIGN KEY (white_username) REFERENCES user(username) ON DELETE SET NULL,
                FOREIGN KEY (black_username) REFERENCES user(username) ON DELETE SET NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """;
            try (var ps1 = conn.prepareStatement(createUserTable);
                 var ps2 = conn.prepareStatement(createGameTable)) {
                ps1.executeUpdate();
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to configure database: " + e.getMessage(), e);
        }
    }

    private String serializeChessGame(ChessGame game) {
        return gson.toJson(game);
    }

    private ChessGame deserializeChessGame(String json) {
        return gson.fromJson(json, ChessGame.class);
    }

}
