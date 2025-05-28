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
    public void createUser(UserData userData) throws RuntimeException {
        if (userData == null || userData.username() == null ||
                userData.password() == null || userData.email() == null){
            throw new RuntimeException("Invalid user data");
        }
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
        if (username == null || password == null) {
            throw new DataAccessException("Username or password is null");
        }

        UserData user = userDB.get(username);
        if (user == null) {
            throw new DataAccessException("User doe snot exist: " + username);
        }

        return user.password().equals(password);
    }
}
