package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.UserData;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest request) throws UserAlreadyExistsException{
        if (request.getUser() == null || request.getPassword() == null || request.getEmail() == null) {
            throw new IllegalArgumentException("Invalid request data");
        }

        if (userDAO.getUser(request.getUser() != null)) {
            throw new UserAlreadyExistsException("Error: user already taken");
        }

        UserData newUser = new UserData(request.getUser(), request.getPassward(), request.getEmail());
        userDAO.createUser(newUser);

        String authToken = UUID.randomUUID().toString();
        authDAO.storeAuthToken(request.getUser(), authToken);

        return new RegisterResult(request.getUser(), authToken);
    }
    public LoginResult login(LoginRequest loginRequest) {}
    public void logout(LogoutRequest logoutRequest) {}
}
