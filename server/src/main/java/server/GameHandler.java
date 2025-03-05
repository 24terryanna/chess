package server;

import com.google.gson.Gson;
import model.GamesList;
import service.GameService;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import spark.Route;

public class GameHandler {
    private GameService gameService;
    private final Gson gson = new Gson();

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Route listGames = (Request req, Response res) -> {
        //debugging
//        System.out.println("Request received: " + req.pathInfo());
//        System.out.println("Authorization header: " + req.headers("authorization"));
        res.type("application/json");
        try {
            String authToken = req.headers("authorization");
            if (authToken == null || authToken.isBlank()) {
                res.status(401);
                return gson.toJson(new GamesList("Error: unauthorized", 401));
            }

            GamesList result = gameService.listGames(authToken);
            res.status(result.statusCode());
            return gson.toJson(result);

        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(new GamesList("Error: " + e.getMessage(), 500));
        }
    };
}
