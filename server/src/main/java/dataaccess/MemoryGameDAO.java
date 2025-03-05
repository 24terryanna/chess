package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import dataaccess.DataAccessException;

public class MemoryGameDAO implements GameDAO{
    private final List<GameData> gamesDB = new ArrayList<>();


    @Override
    public List<GameData> listGames(String username) throws DataAccessException {
        return new ArrayList<>(gamesDB);
//        return gamesDB.stream()
//                .filter(game -> username.equals(game.whiteUsername()) || username.equals(game.blackUsername()))
//                .collect(Collectors.toList());
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
