package model;

public record RegisterResult(String username, String authToken, int statusCode) {
    public RegisterResult(String message, int statusCode) {
        this(null, null, statusCode);
    }
}
