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
            RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);

            if (request.getUsername() == null || request.getPassword() == null || request.getEmail() == null) {
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

    public Route login(Request request, Response response) {
        try {

        } catch() {

        }
    }
}