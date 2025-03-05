package model.req_res;

public record RegisterResult(String username, String authToken, int statusCode, String message) {
    public RegisterResult(String message, int statusCode) {
        this(null, null, statusCode, message);
    }
}
