package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class SqlUserDAO implements UserDAO {

    public SqlUserDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password_hash, email FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(
                                rs.getString("username"),
                                rs.getString("password_hash"),
                                rs.getString("email")
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

        var statement = "INSERT INTO user (username, password_hash, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {

            ps.setString(1, userData.username());
            ps.setString(2, hashPassword(userData.password()));
            ps.setString(3, userData.email());
            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) {  // 23000 = MySQL Integrity Constraint Violation
                throw new DataAccessException("User already exists: " + userData.username());
            } else {
                throw new DataAccessException("Error inserting user: " + e.getMessage());
            }
        }
    }

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var psDisableFK = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0")) {
                psDisableFK.executeUpdate();
            }

            try (var psAuth = conn.prepareStatement("DELETE FROM auth")) {
                psAuth.executeUpdate();
            }
            try (var psGame = conn.prepareStatement("DELETE FROM game")) {
                psGame.executeUpdate();
            }
            try (var psUser = conn.prepareStatement("DELETE FROM user")) {
                psUser.executeUpdate();
            }

            try (var psEnableFK = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1")) {
                psEnableFK.executeUpdate();
            }
//        var statement = "DELETE FROM user";
//        try (var conn = DatabaseManager.getConnection();
//             var ps = conn.prepareStatement(statement)) {
//            ps.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("Error clearing user table: " + e.getMessage(), e);
        }
    }

//    public boolean validateUser(String username, String password) throws DataAccessException {
//        String query = "SELECT password_hash FROM user WHERE username = ?";
//        try (var conn = DatabaseManager.getConnection();
//             var ps = conn.prepareStatement(query)) {
//
//            ps.setString(1, username);
//            try (var rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    String storedHash = rs.getString("password_hash");
//                    return BCrypt.checkpw(password, storedHash); // Compare password securely
//                }
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException("Error validating user: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public boolean authenticateUser(String username, String password) throws DataAccessException {
//        UserData user = getUser(username);
//        return passwordMatches(password, user.password());
//    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            var dropTable = "DROP TABLE IF EXISTS user";
            try (var psDrop = conn.prepareStatement(dropTable)) {
                psDrop.executeUpdate();
            }
            var createTable = """
                CREATE TABLE IF NOT EXISTS user (
                    username VARCHAR(255) NOT NULL,
                    password_hash VARCHAR(255) NOT NULL,
                    email VARCHAR(255),
                    PRIMARY KEY (username)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """;

            try (var psCreate = conn.prepareStatement(createTable)) {
                psCreate.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("Unable to configure database: " + e.getMessage(), e);
        }
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean passwordMatches(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }
}
