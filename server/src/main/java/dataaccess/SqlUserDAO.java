package dataaccess;

import model.UserData;
import java.sql.*;

public class SqlUserDAO implements UserDAO{

    public SqlUserDAO {
        try { DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

//        try (var conn)
    }
//    public UserData getUser(String username) throws DataAccessException {
//
//        String query = "SELECT username, password_has, email FROM users WHERE username = ?";
//        try (Connection connection = DatabaseManager.getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//
//            statement.setString(1, username);
//            ResultSet resultSet = statement.executeQuery();
//
//            if (resultSet.next()) {
//                return new UserData(
//                        resultSet.getString("username"),
//                        resultSet.getString("password_hash"),
//                        resultSet.getString("email"));
//            } else {
//                return null;
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException("Error retrieving user");
//        }
//
//        public void createUser(UserData userData) {
//            String query = "INSERT INTO users (username, password_hash, email) VALUES (?, ?, ?)";
//            try (Connection connection = DatabaseManager.getConnection();
//            PreparedStatement statement = connection.prepareStatement(query)) {
//                statement.setString(1, userData.getUsername());
//                statement.setString(2, userData.getPasswordHash());
//                statement.setString(3, userData.getEmail());
//                statement.exectuteUpdate();
//            } catch (SQLException e) {
//                throw new RuntimeException("Error creating user", e);
//            }
//        }
//
//        public void clear() {
//
//        }
//    }

}
