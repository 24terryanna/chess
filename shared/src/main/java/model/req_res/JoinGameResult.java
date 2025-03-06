package model.req_res;

public record JoinGameResult(String message, int statusCode) {
    public JoinGameResult(int statusCode) {
        this("Successfully joined game", statusCode);
    }
}
