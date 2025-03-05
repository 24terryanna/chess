package dataaccess;

import model.GameData;
import dataaccess.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MemoryGameDAO implements GameDAO{
    private final List<GameData> gamesDB = new ArrayList<>();


    @Override
    public List<GameData> listGames(String username) throws DataAccessException {
        return gamesDB.stream()
                .filter(game -> username.equals(game.whiteUsername()) || username.equals(game.blackUsername()))
                .collect(Collectors.toList());
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

    }

    @Override
    public void clear() throws DataAccessException {
        gamesDB.clear();
    }
}
