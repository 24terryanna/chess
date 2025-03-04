package dataaccess;

import model.GameData;
import dataaccess.DataAccessException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{
    private final Map<Integer, GameData> gamesDB;

    public MemoryGameDAO(){
        gamesDB = new HashMap<>();
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return Collections.unmodifiableCollection(gamesDB.values());
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return gamesDB.get(gameID);
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        if (!gamesDB.containsKey(game.gameID())) {
            throw new DataAccessException("Game is nonexistent");
        }

        if (game.game() == null) {
            throw new DataAccessException("Game cannot be null");
        }
        gamesDB.remove(game.gameID());
        gamesDB.put(game.gameID(), game);
    }

    @Override
    public void clear() throws DataAccessException {
        gamesDB.clear();
    }
}
