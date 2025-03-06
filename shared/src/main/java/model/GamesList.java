package model;


import java.util.List;

public record GamesList(List<GameData> games, String message, int statusCode) {
    public GamesList(List<GameData> games, int statusCode) {
        this(games, null, statusCode);
    }

    public GamesList(String message, int statusCode) {
        this(null, message, statusCode);
    }
}
