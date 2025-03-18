package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SqlUserDAO implements UserDAO{

    // Get user from the database by username
    @Override
    public UserData getUser(String username) throws DataAccessException {
        String query = "SELECT username, password_hash, email FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();  // Use DatabaseManager
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new UserData(
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("email")
                );
            } else {
                return null; // User not found
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving user", e);
        }
    }

    // Insert a new user into the database
    @Override
    public void createUser(UserData userData) {
        String query = "INSERT INTO users (username, password_hash, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();  // Use DatabaseManager
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userData.getUsername());
            stmt.setString(2, userData.getPasswordHash());
            stmt.setString(3, userData.getEmail());
            stmt.executeUpdate();

        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("Error creating user", e);
        }
    }

    // Delete all users from the table
    @Override
    public void clear() {
        String query = "DELETE FROM users";
        try (Connection conn = DatabaseManager.getConnection();  // Use DatabaseManager
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("Error clearing users", e);
        }
    }

}

//    public SqlUserDAO() {
//        try { DatabaseManager.createDatabase(); } catch (DataAccessException e) {
//            throw new RuntimeException(e);
//        }
//        try (var conn = DatabaseManager.getConnection()) {
//            var createUserTable = """
//                    CREATE TABLE IF NOT EXISTS user (
//                                    username VARCHAR(255) NOT NULL,
//                                    password VARCHAR(255) NOT NULL,
//                                    email VARCHAR(255),
//                                    PRIMARY KEY (username)
//                                    )""";
//            try (var createTableStatement = conn.prepareStatement(createUserTable)) {
//                createTableStatement.executeUpdate();
//            }
//        } catch (SQLException | DataAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public UserData getUser(String username) throws DataAccessException {
//        return null;
//    }
//
//    @Override
//    public void createUser(UserData userData) {
//        try (var conn = DatabaseManager.getConnection()) {
//            try (var statement = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES(?, ?, ?)")) {
//                statement.setString(1, user.username());
//                statement.setString(2, hashPassword(user.password()));
//                statement.setString(3, user.email());
//                statement.executeUpdate();
//            }
//        } catch (SQLException | DataAccessException e) {
//            throw new DataAccessException("User already exists: " + user.username());
//        }
//    }
//
//    }
//
//    @Override
//    public void clear() {
//        try (var conn = DatabaseManager.getConnection()) {
//            try (var statement = conn.prepareStatement("TRUNCATE user")) {
//                statement.executeUpdate();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        } catch (SQLException | DataAccessException e) {
//        }
//    }
////    void storeUserPassword(String username, String clearTextPassword) {
////    String hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
////
////    // write the hashed password in database along with the user's other information
////    writeHashedPasswordToDatabase(username, hashedPassword);
//}
//    private String hashPassword(String password) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        return encoder.encode(password);
//    }
//
//    private boolean passwordMatches(String rawPassword, String hashedPassword) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        return encoder.matches(rawPassword, hashedPassword);
//    }
//
//}
