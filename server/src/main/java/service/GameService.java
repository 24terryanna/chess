package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import model.GamesList;

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
}
