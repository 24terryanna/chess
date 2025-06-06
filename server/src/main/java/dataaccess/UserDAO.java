package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;
    void createUser(UserData userData) throws DataAccessException;
    void clear() throws DataAccessException;
    boolean verifyUser(String username, String password) throws DataAccessException;
}
