package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO{
    private HashSet<UserData> db;

    public MemoryAuthDAO(){
        db = HashSet.newHashSet(16);
    }

    @Override
    public void addAuth(AuthData authData) {

    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() {

    }
}
