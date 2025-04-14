package server;

import dataaccess.*;
import service.GameService;
import service.UserService;
import spark.*;


public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        UserDAO userDAO = new SqlUserDAO();
        AuthDAO authDAO = new SqlAuthDAO();
        GameDAO gameDAO = new SqlGameDAO();

        UserService userService = new UserService(userDAO, authDAO);
        GameService gameService = new GameService(gameDAO, authDAO);

        UserHandler userHandler = new UserHandler(userService);
        DatabaseHandler databaseHandler = new DatabaseHandler(userDAO, authDAO, gameDAO);
        GameHandler gameHandler = new GameHandler(gameService);

        userEndpoints(userHandler, databaseHandler, gameHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        //Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    private void userEndpoints(UserHandler userHandler, DatabaseHandler databaseHandler, GameHandler gameHandler) {
        Spark.post("/user", userHandler.registerUser);
        Spark.post("/session", userHandler.loginUser);
        Spark.delete("/session", userHandler.logoutUser);
        Spark.delete("/db", databaseHandler.clearDatabase);
        Spark.get("/game", gameHandler.listGames);
        Spark.post("/game", gameHandler.createGame);
        Spark.put("/game", gameHandler.joinGame);
        Spark.put("/game/:gameID", gameHandler.updateGame);

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
