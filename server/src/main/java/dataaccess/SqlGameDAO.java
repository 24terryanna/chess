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
        var statement = "INSERT INTO game (white_username, black_username, game_name, game_state) VALUES (?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, game.whiteUsername());
            ps.setString(2, game.blackUsername());
            ps.setString(3, game.gameName());
            ps.setString(4, serializeChessGame(game.game()));
            ps.executeUpdate();

            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return new GameData(rs.getInt(1), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error creating game: " + e.getMessage());
        }
        throw new DataAccessException("Failed to create game");
    }


    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        var statement = "SELECT * FROM game WHERE game_id = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {

            ps.setInt(1, gameID);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new GameData(
                            rs.getInt("game_id"),
                            rs.getString("white_username"),
                            rs.getString("black_username"),
                            rs.getString("game_name"),
                            deserializeChessGame(rs.getString("game_state"))
                    );
                }
            } catch (SQLException e) {
                throw new DataAccessException("Error retrieving game: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void updateGame(GameData updatedGame) throws DataAccessException {
        var statement = "UPDATE game SET white_username=?, black_username=?, game_name=?, game_state=? WHERE game_id=?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {

            ps.setString(1, updatedGame.whiteUsername());
            ps.setString(2, updatedGame.blackUsername());
            ps.setString(3, updatedGame.gameName());
            ps.setString(4, serializeChessGame(updatedGame.game()));
            ps.setInt(5, updatedGame.gameID());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("No game found with ID: " + updatedGame.gameID());
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error updating game: " + e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE game";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            //throw new DataAccessException("Error clearing game table: " + e.getMessage());
        }
    }
    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            var createTable = """
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
            try (var ps = conn.prepareStatement(createTable)) {
                ps.executeUpdate();
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
