package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.List;
import dataaccess.DataAccessException;

public class MemoryGameDAO implements GameDAO{
    private final List<GameData> gamesDB = new ArrayList<>();


    @Override
    public List<GameData> listGames(String username) throws DataAccessException {
        return new ArrayList<>(gamesDB);

    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        if (game == null || game.game() == null) {
            throw new DataAccessException("Game cannot be null");
        }

        int gameID = gamesDB.size() + 1;
        game = new GameData(gameID, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
        gamesDB.add(game);
        return game;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return gamesDB.get(gameID);
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        gamesDB.clear();
    }
}
