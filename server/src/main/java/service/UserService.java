package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest request) {
        if (request.username() == null || request.password() == null || request.email() == null) {
            return new RegisterResult("Error: bad request", 400);
        }

        if (userDAO.getUser(request.username() != null)) {
            return new RegisterResult("Error: already taken", 403);
        }

        //create the new user
        UserData newUser = new UserData(request.username(), request.password(), request.email());
        userDAO.createUser(newUser);

        //create authtoken for new user
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(request.username(), authToken);
        authDAO.createAuth(authData);

        return new RegisterResult(request.username(), authToken, 200);
    }
//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}
}



//    public AuthData createUser(UserData userData) throws BadRequestException { //need to create BadRequest
//        try {
//            userDAO.createUser((userData));
//        } catch (DataAccessException e) {
//            throw new BadRequestException(e.getMessage());
//        }
//        String authToken = UUID.randomUUID().toString();
//        AuthData authData = new AuthData(userData.username(), authToken);
//        authDAO.createAuth(authData);
//
//        return authData;
//    }



