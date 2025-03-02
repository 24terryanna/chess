package dataaccess;

import model.GameData;
import model.UserData;

import java.util.HashSet;

public class MemoryGameDAO implements GameDAO{
    private HashSet<UserData> db;

    public MemoryGameDAO(){
        db = HashSet.newHashSet(16);
    }

    @Override
    public HashSet<GameData> listGames() {
        return null;
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public boolean gameExists(int gameID) {
        return false;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }

    @Override
    public void clear() {

    }
}
