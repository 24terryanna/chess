package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDAO implements GameDAO{
    private final List<GameData> gamesDB = new ArrayList<>();
    private int nextID = 1;


    @Override
    public List<GameData> listGames(String username) throws DataAccessException {
        return new ArrayList<>(gamesDB);

    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        if (game == null || game.game() == null) {
            throw new DataAccessException("Game cannot be null");
        }

        GameData newGame = new GameData(nextID++, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
        gamesDB.add(newGame);

        return newGame;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        for (GameData game : gamesDB) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        throw new DataAccessException("Game not found with ID: " + gameID);
        //return gamesDB.get(gameID);
    }

    @Override
    public void updateGame(GameData updatedGame) throws DataAccessException {
        if (updatedGame == null) {
            throw new DataAccessException("Game data can't be null");
        }

        for (int i =0; i <gamesDB.size(); i++) {
            if (gamesDB.get(i).gameID() == updatedGame.gameID()) {
                gamesDB.set(i, updatedGame);
                return;
            }
        }
        throw new DataAccessException("Game with given ID " + updatedGame.gameID() + " not found");
    }

    @Override
    public void clear() throws DataAccessException {
        gamesDB.clear();
    }
}
