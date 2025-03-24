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
        var checkUserStatement = "SELECT COUNT(*) FROM auth WHERE username=?";
        var statement = "INSERT INTO auth (auth_token, username) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {

            ps.setString(1, authData.authToken());
            ps.setString(2, authData.username());
            ps.executeUpdate();

        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error creating auth_token: " + e.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String auth_token) throws DataAccessException {
        var statement = "SELECT auth_token, username FROM auth WHERE auth_token=?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {

            ps.setString(1, auth_token);
            try (var result = ps.executeQuery()) {

                if (result.next()) {
                    return new AuthData(
                            result.getString("auth_token"),
//                            auth_token,
                            result.getString("username")
                            //auth_token
                    );
//                } else {
//                    throw new DataAccessException("Auth token not found: " + auth_token);
//                }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving auth_token: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(String auth_token) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE auth_token=?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {

            ps.setString(1, auth_token);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error deleting auth_token: " + e.getMessage());
        }
    }

    @Override
    public void clear() {
        var statement = "DELETE FROM auth";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("Error clearing auth table: " + e.getMessage(), e);
        }
    }
}