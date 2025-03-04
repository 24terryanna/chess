package server;

import dataaccess.*;
import server.UserHandler;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
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

        //initialize handlers
        UserHandler userHandler = new UserHandler(userService);
        DatabaseHandler databaseHandler = new DatabaseHandler(userDAO, authDAO, gameDAO);

        //register endpoints
        Spark.post("/user", userHandler.registerUser);
        Spark.delete("/db", databaseHandler.clearDatabase);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
