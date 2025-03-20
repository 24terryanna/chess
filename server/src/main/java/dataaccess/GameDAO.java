package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.List;

public interface GameDAO {
    List<GameData> listGames(String username) throws DataAccessException;

    GameData createGame (GameData game) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException, SQLException;

    void updateGame(GameData game) throws DataAccessException;

    void clear() throws DataAccessException;
}
