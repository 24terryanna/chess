package dataaccess;

import model.UserData;
import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO{
    private final Map<String, UserData> db = new HashMap<>();

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return db.get(username);
    }

    @Override
    public void createUser(UserData userData) {
        if (db.containsKey(userData.username())) {
            throw new RuntimeException("User already exists!");
        }
        db.put(userData.username(), userData);
    }

    @Override
    public void clear() {
        db.clear();

    }
}
