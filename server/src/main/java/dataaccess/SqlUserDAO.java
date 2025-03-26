package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;


import java.sql.*;

public class SqlUserDAO implements UserDAO {

    public SqlUserDAO() {
        try {DatabaseManager.createDatabase(); } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
        try (var conn = DatabaseManager.getConnection()) {
            var createTestTable = """            
                    CREATE TABLE if NOT EXISTS user (
                        username VARCHAR(255) NOT NULL,
                        password_hash VARCHAR(255) NOT NULL,
                        email VARCHAR(255),
                        PRIMARY KEY (username)
                        )""";
            try (var createTableStatement = conn.prepareStatement(createTestTable)) {
                createTableStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password_hash, email FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var result = ps.executeQuery()) {
                    if (result.next()) {
                        return new UserData(
                                result.getString("username"),
                                result.getString("password_hash"),
                                result.getString("email")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to retrieve user: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        System.out.println("Creating user: " + userData.username());

        var checkUserStatement = "SELECT COUNT(*) FROM user WHERE username=?";
        var insertStatement = "INSERT INTO user (username, password_hash, email) VALUES (?, ?, ?)";


        try (var conn = DatabaseManager.getConnection();
             var checkStmt = conn.prepareStatement(checkUserStatement);
             var insertStmt = conn.prepareStatement(insertStatement)) {

            // Check if the username already exists
            checkStmt.setString(1, userData.username());
            try (var result = checkStmt.executeQuery()) {
                if (result.next() && result.getInt(1) > 0) {
                    throw new DataAccessException("User already exists: " + userData.username());
                }
            }

            // Insert the new user
            insertStmt.setString(1, userData.username());
            insertStmt.setString(2, hashPassword(userData.password()));
            insertStmt.setString(3, userData.email());
            insertStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error inserting user: " + e.getMessage());
        }
    }

    @Override
    public void clear() {
        var statement = "DELETE FROM user";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("Error clearing user table: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean verifyUser(String username, String password) throws DataAccessException {
        UserData userData = getUser(username);
        if (userData == null) {
            throw new DataAccessException("User does not exist: " + username);
        }
        return passwordMatches(password, userData.password());
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean passwordMatches(String clearTextPassword, String hashedPassword) {
        return BCrypt.checkpw(clearTextPassword, hashedPassword);
    }

}
