package server;

import com.google.gson.Gson;
import model.GamesList;
import service.GameService;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import spark.Route;
import service.GameService;

public class GameHandler {
    private GameService gameService;
    private final Gson gson = new Gson();

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Route listGames = (Request req, Response res) -> {
        try {
            String authToken = req.headers("authorization");
            if (authToken == null) {
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
