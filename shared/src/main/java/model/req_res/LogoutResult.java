package model.req_res;

public record LogoutResult(String message, int statusCode) {
    public LogoutResult(int statusCode) {
        this(null, statusCode); //should message be null?
    }
}
