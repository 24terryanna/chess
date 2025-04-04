package dataaccess;

import model.AuthData;

import java.sql.*;

public class SqlAuthDAO implements AuthDAO {

    public SqlAuthDAO() {
        try {DatabaseManager.createDatabase(); } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
        try (var conn = DatabaseManager.getConnection()) {
            var createTestTable = """
                    CREATE TABLE if NOT EXISTS auth (
                        username VARCHAR(255) NOT NULL,
                        auth_token VARCHAR(255) NOT NULL,
                        PRIMARY KEY (auth_token)
                        ) """;
            try (var createTableStatement = conn.prepareStatement(createTestTable)) {
                createTableStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auth (username, auth_token) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement)) {

            preparedStatement.setString(1, authData.username());
            preparedStatement.setString(2, authData.authToken());
            preparedStatement.executeUpdate();

        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error creating authToken: " + e.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var statement = "SELECT auth_token, username FROM auth WHERE auth_token=?";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement)) {

            preparedStatement.setString(1, authToken);
            try (var result = preparedStatement.executeQuery()) {

                if (result.next()) {
                    return new AuthData(
                            result.getString("auth_token"),
                            result.getString("username")
                    );
                }

            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving auth_token: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE auth_token=?";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement)) {

            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error deleting authToken: " + e.getMessage());
        }
    }

    @Override
    public void clear() {
        var statement = "DELETE FROM auth";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("Error clearing auth table: " + e.getMessage(), e);
        }
    }
}