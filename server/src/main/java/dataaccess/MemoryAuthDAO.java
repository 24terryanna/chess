package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO{
    private Map<String, AuthData> db;

    public MemoryAuthDAO() {
        db = new HashMap<>();
    }

    @Override
    public void createAuth(AuthData authData) {
        db.put(authData.authToken(), authData);
    }

    @Override
    public void deleteAuth(String authToken) {
        db.remove(authToken);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return db.get(authToken);
    }

    @Override
    public void clear() {
        db.clear();
    }
}
