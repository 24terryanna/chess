package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.*;

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
            return new GamesList("Error: unauthorized", 401);
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

        GameData newGame = new GameData(0, authData.username(), null, gameName, new ChessGame());
        return newGame.gameID();
    }

}
