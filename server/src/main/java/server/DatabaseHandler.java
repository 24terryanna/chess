package server;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import spark.Request;
import spark.Response;
import spark.Route;

public class DatabaseHandler {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    public DatabaseHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Route clearDatabase = (Request request, Response response) -> {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();

        response.status(200);
        return "{ \"message\": \"Database cleared\" }";
    };
}
