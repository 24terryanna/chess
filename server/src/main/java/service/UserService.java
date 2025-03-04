package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import response.RegisterResult;

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
            if (request == null || request.getUsername() == null || request.getPassword() == null || request.getEmail() == null) {
                return new RegisterResult("Error: bad request", 400);
            }


            //check existing user
            UserData existingUser = userDAO.getUser(request.getUsername());
            if (existingUser != null) {
                System.out.println("User already exists: " + request.getUsername());
                return new RegisterResult("Error: already taken", 403);
            }

            //create the new user
            UserData newUser = new UserData(request.getUsername(), request.getPassword(), request.getEmail());
            userDAO.createUser(newUser);

            // Verify user was correctly added
            UserData checkUser = userDAO.getUser(request.getUsername());
            if (checkUser == null) {
                System.out.println("ERROR: User was not properly added to DAO!");
            } else {
                System.out.println("SUCCESS: User was correctly stored in DAO.");
            }

            //create authToken for new user
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(request.getUsername(), authToken);
            authDAO.createAuth(authData);

            //success response
            return new RegisterResult(request.getUsername(), authToken);
        } catch (Exception e) {
            return new RegisterResult("Error: iternal server error", 500);
        }

    }
}



