package model.response;

public record LoginResult(String username, String authToken, int statusCode, String message) {
    public LoginResult(String message, int statusCode) {
        this(null, null, statusCode, message);
    }
}
