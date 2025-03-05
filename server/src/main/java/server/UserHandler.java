package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.req_res.*;
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
            RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);

            if (request.username() == null || request.password() == null || request.email() == null) {
                res.status(400);
                return gson.toJson(new RegisterResult("Error: bad request", 400));
            }

            RegisterResult result = userService.register(request);
            res.status(result.statusCode());
            return gson.toJson(result);

        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(new RegisterResult("Error: " + e.getMessage(), 500));
        }
    };

    public Route loginUser = (Request req, Response res) -> {
        try {
            LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);

            if (request.username() == null || request.password() == null) {
                res.status(400);
                return gson.toJson(new LoginResult("Error: bad request", 400));
            }

            LoginResult result = userService.login(request);
            res.status(result.statusCode());
            return gson.toJson(result);

        } catch(DataAccessException e) {
            res.status(500);
            return gson.toJson(new LoginResult("Error: " + e.getMessage(), 500));
        }
    };

    public Route logoutUser = (Request req, Response res) -> {
        try {
            String authToken = req.headers("authorization");

            if (authToken == null || authToken.isEmpty()) {
                res.status(401);
                return gson.toJson(new LogoutResult("Error: unauthorized", 401));
            }

            LogoutResult result = userService.logout(authToken);
            res.status(result.statusCode());
            return gson.toJson(result);
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(new LogoutResult("Error: " + e.getMessage(), 500));
        }
    };
}