package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDAO implements GameDAO{
    private final List<GameData> gamesDB = new ArrayList<>();
    private int nextID = 1;


    @Override
    public List<GameData> listGames(String username) throws DataAccessException {
        if (username == null || username.isBlank()) {
            throw new DataAccessException("Username cannot be null or blank");
        }
        return new ArrayList<>(gamesDB);

    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        if (game == null || game.game() == null ||
        game.gameName() == null || game.gameName().isBlank()) {
            throw new DataAccessException("Invalid game data");
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
    }

    @Override
    public void updateGame(GameData updatedGame) throws DataAccessException {
        if (updatedGame == null || updatedGame.game() == null) {
            throw new DataAccessException("Invalid updated game data");
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
