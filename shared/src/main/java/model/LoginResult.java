package model;

public record LoginResult(String username, String authToken, int statusCode) {
    public LoginResult(String message, int statusCode) {
        this(null, null, statusCode);
    }
}
