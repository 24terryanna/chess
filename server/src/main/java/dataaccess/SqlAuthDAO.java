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
                        )
                    """;
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
             var ps = conn.prepareStatement(statement)) {

            ps.setString(1, authData.username());
            ps.setString(2, authData.authToken());
            ps.executeUpdate();

        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error creating auth token: " + e.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var statement = "SELECT auth_token, username FROM auth WHERE auth_token=?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {

            ps.setString(1, authToken);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AuthData(
                            rs.getString("auth_token"),
                            rs.getString("username")
                    );
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving auth token: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE auth_token=?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {

            ps.setString(1, authToken);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error deleting auth token: " + e.getMessage());
        }
    }

    @Override
    public void clear() {
        var statement = "TRUNCATE TABLE auth";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("Error clearing auth table: " + e.getMessage(), e);
        }
    }
}