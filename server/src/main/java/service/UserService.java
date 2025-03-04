package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import model.RegisterResult;
import model.RegisterRequest;
import model.LoginResult;
import model.LoginRequest;

import javax.xml.crypto.Data;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        try {
            //check if request exists, if not bad request error
            if (request == null || request.username() == null || request.password() == null || request.email() == null) {
                return new RegisterResult("Error: bad request", 400);
            }

            //check existing user
            UserData existingUser = userDAO.getUser(request.username());
            if (existingUser != null) {
                return new RegisterResult("Error: already taken", 403);
            }

            //create the new user
            UserData newUser = new UserData(request.username(), request.password(), request.email());
            userDAO.createUser(newUser);

            //create authToken for new user
            return createAuthResponse(request.username());
        } catch (Exception e) {
            return new RegisterResult("Error: iternal server error", 500);
        }

    }

    public LoginResult login(LoginRequest request) throws DataAccessException {
        try {
            if (request == null || request.username() == null || request.password() == null) {
                return new LoginResult("Error: bad request", 400);
            }

            UserData userData = userDAO.getUser(request.username());

            if (userData == null || !userData.password().equals(request.password())) {
                return new LoginResult("Error: unauthorized", 401);
            }

            return createAuthResponse(request.username());
        } catch (Exception e) {
            return new LoginResult("Error: internal server error", 500);
        }
    }
    private <T> T createAuthResponse(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(username, authToken));

        return (T) (authToken.length() > 0 ? new RegisterResult(username, authToken, 200)
                : new LoginResult(username, authToken, 200));
    }

}



