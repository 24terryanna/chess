package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO{
    private Map<String, UserData> db;

    public MemoryUserDAO(){
        db = new HashMap<>();
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return db.get(username);
    }

    @Override
    public void createUser(UserData userData) {
        db.put(userData.username(), userData);
    }

    @Override
    public boolean authUser(String username, String password) throws DataAccessException {
        UserData user = db.get(username);
        return user != null && user.password().equals(password);
    }

    @Override
    public void clear() {
        db.clear();

    }
}
