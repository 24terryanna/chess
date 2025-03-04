package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;

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
            //create authToken for new user
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(request.username(), authToken);
            authDAO.createAuth(authData);

            //create the new user
            UserData newUser = new UserData(request.username(), request.password(), request.email());
            userDAO.createUser(newUser);
            return new RegisterResult(request.username(), authData.authToken(), 200, "Successful registration!");

        } catch (Exception e) {
            return new RegisterResult("Error: internal server error", 500);
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

            String authToken = UUID.randomUUID().toString();
            authDAO.createAuth(new AuthData(request.username(), authToken));
            return new LoginResult(request.username(), authToken, 200, "Successful login!");

        } catch (Exception e) {
            return new LoginResult("Error: internal server error", 500);
        }
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



