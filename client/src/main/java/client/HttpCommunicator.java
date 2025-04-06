package client;

import com.google.gson.Gson;
import model.GameData;
import server.ServerFacade;

import java.util.List;
import java.util.Map;

public class HttpCommunicator {

    String url;
    ServerFacade serverFacade;

    public HttpCommunicator(ServerFacade serverFacade, String serverDomain) {
        url = "http://" + serverDomain;
        this.serverFacade = serverFacade;
    }

    public boolean register(String username, String password, String email) {
        var body = Map.of("username", username, "password", password, "email", email);
        var jsonBody = new Gson().toJson(body);
        Map response = request("POST", "/user", jsonBody);

        if (response.containsKey("Error")) {
            return false;
        }

        serverFacade.setAuthToken((String) response.get("authToken"));
        return true;
    }

    public boolean login(String username, String password){
        var body = Map.of("username", username, "password", password);
        var jsonBody = new Gson().toJson(body);
        Map response = request("POST", "/session", jsonBody);

        if (response.containsKey("Error")) {
            return false;
        }

        serverFacade.setAuthToken((String) response.get("authToken"));
        return true;
    }

    public boolean logout() {
        Map response = request("DELETE", "/session");
        if (response.containsKey("Error")) {
            return false;
        }

        serverFacade.setAuthToken(null);
        return true;
        }
    }

    public int createGame(String gameName) {

    }

    public List<GameData> listGames() {

    }

    public boolean joinGame(int gameID, String playerColor) {

    }

    private Map request(String method, String endpoint) {
        return request(method, endpoint);
    }

    private Map request(String method, String endpoint, String body) {

    }

}
