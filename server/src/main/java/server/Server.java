package server;

import dataaccess.*;
import service.GameService;
import service.UserService;
import spark.*;


public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        //initialize DAOs
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        //initialize services
        UserService userService = new UserService(userDAO, authDAO);
        GameService gameService = new GameService(gameDAO, authDAO);

        //initialize handlers
        UserHandler userHandler = new UserHandler(userService);
        DatabaseHandler databaseHandler = new DatabaseHandler(userDAO, authDAO, gameDAO);
        GameHandler gameHandler = new GameHandler(gameService);

        //user method endpoints
        userEndpoints(userHandler, databaseHandler, gameHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    private void userEndpoints(UserHandler userHandler, DatabaseHandler databaseHandler, GameHandler gameHandler) {
        Spark.post("/user", userHandler.registerUser);
        Spark.post("/session", userHandler.loginUser);
        Spark.delete("/session", userHandler.logoutUser);
        Spark.delete("/db", databaseHandler.clearDatabase);
        Spark.get("/game", gameHandler.listGames);

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
