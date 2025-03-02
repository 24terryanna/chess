package dataaccess;

import model.UserData;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO{
    private HashSet<UserData> db;

    public MemoryUserDAO(){
        db = HashSet.newHashSet(16);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(UserData userData) {

    }

    @Override
    public boolean authUser(String username, String password) throws DataAccessException {
        return false;
    }

    @Override
    public void clear() {

    }
}
