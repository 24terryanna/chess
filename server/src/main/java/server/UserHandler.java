package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import request.RegisterRequest;
import response.RegisterResult;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;


public class UserHandler {
    UserService userService;
    Gson gson = new Gson();

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Route registerUser = (Request req, Response res) -> {
        try {
            // Convert request body JSON into RegisterRequest object
            RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);

            // Validate request fields
            if (request.getUsername() == null || request.getPassword() == null || request.getEmail() == null) {
                res.status(400);
                return gson.toJson(new RegisterResult("Error: bad request", 400));
            }

            // Call UserService to register the user
            RegisterResult result = userService.register(request);

            // If successful, return 200 with username and authToken
            res.status(200);
            return gson.toJson(result);

        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(new RegisterResult("Error: " + e.getMessage(), 500));
        }
    };
}