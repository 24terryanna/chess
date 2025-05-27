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
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null || authToken.isBlank() || authDAO.getAuth(authToken) == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        String username = authData.username();
        List<GameData> games = gameDAO.listGames(username);
        return new GamesList(games, 200);
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        if (gameName != null && !gameName.isBlank()) {
            throw new DataAccessException("Error: bad request");
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

        if (playerColor == null || gameID == 0 ||
                !playerColor.equalsIgnoreCase("WHITE") && !playerColor.equalsIgnoreCase("BLACK")) {
            throw new DataAccessException("Error: bad request");
        }

        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("error: unauthorized");
        }
        String username = authData.username();
        System.out.println("AuthData username: " + authData.username());

        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null) {
            throw new DataAccessException("Error: bad request");
        }

        if (playerColor.equalsIgnoreCase("WHITE")) {
            if (gameData.whiteUsername() != null) {
                throw new DataAccessException("Error: already taken");
            }
            gameData = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());

        } else {
            if (gameData.blackUsername() != null) {
                throw new DataAccessException("Error: already taken");
            }
            gameData = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());

        }
        gameDAO.updateGame(gameData);
    }

}
