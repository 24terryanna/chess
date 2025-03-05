package dataaccess;

import model.GameData;

import java.util.List;

public interface GameDAO {
    List<GameData> listGames(String username) throws DataAccessException;

    void createGame (GameData game) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void updateGame(GameData game) throws DataAccessException;

    void clear() throws DataAccessException;
}
