package dataaccess;

import model.UserData;
import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO{
    private final Map<String, UserData> userDB = new HashMap<>();

    @Override
    public UserData getUser(String username) throws DataAccessException {
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
    public void clear() {
        userDB.clear();

    }
}
