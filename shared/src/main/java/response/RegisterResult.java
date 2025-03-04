package response;

public record RegisterResult(String username, String authToken, String message, int statusCode) {
    public RegisterResult(String username, String authToken) {
        this(username, authToken, null, 200);
    }

    public RegisterResult(String message, int statusCode) {
        this(null, null, message, statusCode);
    }
}
