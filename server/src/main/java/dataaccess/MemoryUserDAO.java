package dataaccess;

import model.UserData;
import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO{
    private final Map<String, UserData> userDB = new HashMap<>();

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (username == null || username.isBlank()) {
            throw new DataAccessException("Username cannot be null or blank");
        }
        return userDB.get(username);
    }

    @Override
    public void createUser(UserData userData) {
        if (userDB.containsKey(userData.username())) {
            throw new RuntimeException("User already exists!");
        }
        userDB.put(userData.username(), userData);
    }

    @Override
    public void clear() throws DataAccessException {
        userDB.clear();

    }

    @Override
    public boolean verifyUser(String username, String password) throws DataAccessException {
        boolean existingUser = false;
        for (UserData user : userDB.values()) {
            if (user.username().equals(username)) {
                existingUser = true;
            }
            if (user.username().equals(username) && user.password().equals(password)) {
                return true;
            }
        }
        if (existingUser) {
            return false;
        } else {
            throw new DataAccessException("User does not exist: " + username);
        }
    }
}
