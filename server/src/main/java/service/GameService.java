package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.*;

import java.sql.SQLException;
import java.util.List;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public GamesList listGames(String authToken) throws DataAccessException {
        try {
            AuthData authData = authDAO.getAuth(authToken);
            if (authData == null || authToken.isBlank()) {
                return new GamesList("Error: unauthorized", 401);
            }

            String username = authData.username();
            List<GameData> games = gameDAO.listGames(username);
            return new GamesList(games, 200);
        } catch (DataAccessException e) {
            return new GamesList("Error: failed to access database", 500);
        }

    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        GameData newGame = new GameData(0, null, null, gameName, new ChessGame());
        GameData createdGame = gameDAO.createGame(newGame);
        return createdGame.gameID();
    }

    public void updateGame(String authToken, int gameID, GameData updatedGame) throws DataAccessException, SQLException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        GameData existingGame = gameDAO.getGame(gameID);
        if (existingGame == null) {
            throw new DataAccessException("Error: game not found");
        }

        if (!authData.username().equals(existingGame.whiteUsername()) &&
        !authData.username().equals(existingGame.blackUsername())) {
            throw new DataAccessException("Error: not allowed to update this game");
        }

        gameDAO.updateGame(updatedGame);
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws DataAccessException {
        if (authToken == null || authToken.isBlank()) {
            throw new DataAccessException("Error: unauthorized");
        }

        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null) {
            throw new DataAccessException("Error: bad request");
        }

        String username = authData.username();
        System.out.println("JoinGame: username = " + username + ", playerColor = " + playerColor);

        if (playerColor == null || playerColor.equalsIgnoreCase("observer"))  {
            // observer does not claim a player slot, no update needed
            return;
//            throw new DataAccessException("Error: bad request");
        }

        if (playerColor.equalsIgnoreCase("WHITE")) {
            if (gameData.whiteUsername() != null) {
                throw new DataAccessException("Error: already taken");
            }
            gameData = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());

        } else if (playerColor.equalsIgnoreCase("BLACK")){
            if (gameData.blackUsername() != null) {
                throw new DataAccessException("Error: already taken");
            }
            gameData = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());

        } else {
            throw new DataAccessException("Error: bad request");
        }
        gameDAO.updateGame(gameData);
    }

}
