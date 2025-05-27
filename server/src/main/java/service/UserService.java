package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;
import model.response.*;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException {
            if (request == null || request.username() == null || request.password() == null || request.email() == null) {
                return new RegisterResult("Error: bad request", 400);
            }

            UserData existingUser = userDAO.getUser(request.username());
            if (existingUser != null) {
                return new RegisterResult("Error: already taken", 403);
            }

            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, request.username());
            authDAO.createAuth(authData);

            UserData newUser = new UserData(request.username(), request.password(), request.email());
            userDAO.createUser(newUser);
            return new RegisterResult(request.username(), authData.authToken(), 200, "Successful registration!");


    }

    public LoginResult login(LoginRequest request) throws DataAccessException {
            if (request == null || request.username() == null || request.password() == null) {
                return new LoginResult("Error: bad request", 400);
            }

            UserData userData = userDAO.getUser(request.username());

            if (userData == null || !userDAO.verifyUser(request.username(), request.password())) {
                return new LoginResult("Error: unauthorized", 401);
            }

            String authToken = UUID.randomUUID().toString();
            authDAO.createAuth(new AuthData(authToken, request.username()));
            return new LoginResult(request.username(), authToken, 200, "Successful login!");
    }

    public LogoutResult logout(String authToken) throws DataAccessException {
        try {
            if (authToken == null || authToken.isEmpty()) {
                return new LogoutResult("Error: unauthorized", 401);
            }

            AuthData authData = authDAO.getAuth(authToken);
            if (authData == null) {
                return new LogoutResult("Error: unauthorized", 401);
            }

            authDAO.deleteAuth(authToken);
            return new LogoutResult("Successful logout", 200);
        } catch (Exception e) {
            return new LogoutResult("Error: internal server error", 500);
        }
    }

}



