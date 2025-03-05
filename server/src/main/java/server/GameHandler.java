package server;

import com.google.gson.Gson;
import model.GameData;
import model.req_res.CreateGameError;
import model.req_res.CreateGameRequest;
import model.req_res.CreateGameResult;
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

    public Route createGame = (Request req, Response res) -> {
        try {
            String authToken = req.headers("authorization");
            if (authToken == null || authToken.isBlank()) {
                res.status(401);
                return gson.toJson(new CreateGameError("Error: unauthorized", 401));
            }

            CreateGameRequest gameRequest = gson.fromJson(req.body(), CreateGameRequest.class);
            if (gameRequest == null || gameRequest.gameName() == null || gameRequest.gameName().isBlank()) {
                res.status(400);
                return gson.toJson(new CreateGameError("Error: bad request", 400));
            }

            int gameID = gameService.createGame(authToken, gameRequest.gameName());

            if (gameID == 0) {
                throw new DataAccessException("Failed to create game");
            }

            res.status(200);

            return gson.toJson(new CreateGameResult(gameID));
        } catch (DataAccessException e) {
            if (e.getMessage().contains("unauthorized")) {
                res.status(401);
            } else {
                res.status(500);
            }
            return gson.toJson(new CreateGameError("Error: " + e.getMessage(), 500));
        }
    };

    public Route updateGame = (Request req, Response res) -> {
        res.type("application/json");
        try {
            String authToken = req.headers("authorization");
            if (authToken == null || authToken.isBlank()) {
                res.status(401);
                return gson.toJson(new CreateGameError("Error: unauthorized", 401));
            }

            int gameID;
            try {
                gameID = Integer.parseInt(req.params(":gameID"));
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(new CreateGameError("Error: invalid game ID", 400));
            }

            GameData updatedGame = gson.fromJson(req.body(), GameData.class);
            if (updatedGame == null) {
                res.status(400);
                return gson.toJson(new CreateGameError("Error: bad request", 400));
            }
            gameService.updateGame(authToken, gameID, updatedGame);
            res.status(200);
            return gson.toJson(new CreateGameResult(gameID));

        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(new CreateGameError("Error: " + e.getMessage(), 500));
        }
    };
}
