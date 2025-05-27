package dataaccess;

import model.AuthData;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO{
    private final Map<String, AuthData> db;

    public MemoryAuthDAO() {
        db = new HashMap<>();
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        if (authData == null || authData.authToken() == null || authData.username() == null) {
            throw new DataAccessException("Invalid auth data");
        }
        db.put(authData.authToken(), authData);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        db.remove(authToken);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return db.get(authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        db.clear();
    }
}
