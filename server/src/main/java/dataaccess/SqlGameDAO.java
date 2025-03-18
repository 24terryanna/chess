package dataaccess;

import model.GameData;

import java.util.List;

public class SqlGameDAO implements GameDAO{
    @Override
    public List<GameData> listGames(String username) throws DataAccessException {
        return List.of();
    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
