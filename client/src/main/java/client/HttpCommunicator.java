package client;

import com.google.gson.Gson;
import model.GameData;
import model.GamesList;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
        var body = Map.of("gameName", gameName);
        var jsonBody = new Gson().toJson(body);
        Map response = request("POST", "/game", jsonBody);

        if (response.containsKey("Error")) {
            return -1;
        }

        float gameID = (float) response.get("gameID");
        return (int) gameID;

    }

    public List<GameData> listGames() {
        String response = requestString("GET", "/game");
        if(response.contains("Error")) {
            return new ArrayList<>();
        }
        GamesList games = new Gson().fromJson(response, GamesList.class);
        return new ArrayList<>(games.games());
    }

    public boolean joinGame(int gameID, String playerColor) {
        Map body;
        if (playerColor != null) {
            body = Map.of("gameID", gameID, "playerColor", playerColor);
        } else {
            body = Map.of("gameID", gameID);
        }
        var jsonBody = new Gson().toJson(body);
        Map response = request("PUT", "/game", jsonBody);
        return !response.containsKey("Error");
    }

    private String requestString(String method, String endpoint) {
        return requestString(method, endpoint, null);
    }

    private String requestString(String method, String endpoint, String body) {
        try {
            HttpURLConnection connection = makeConnection(method, endpoint, body);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                return "Error: 401 Unauthorized";
            }

            try (InputStream inputStream = connection.getInputStream();
                 InputStreamReader reader = new InputStreamReader(inputStream)) {
                return readerToString(reader);
            }
        } catch (IOException | URISyntaxException e) {
            return "Error: " + e.getMessage();
        }
    }

    private HttpURLConnection makeConnection(String method, String endpoint, String body)
            throws URISyntaxException, IOException {

        URI uri = new URI(baseURL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();

        connection.setRequestMethod(method);

        String token = serverFacade.getAuthToken();
        if (token != null) {
            connection.setRequestProperty("Authorization", token);
        }

        if (body != null) {
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            try (OutputStream os = connection.getOutputStream()) {
                os.write(body.getBytes());
            }
        }

        connection.connect();

        return connection;
    }

    private String readerToString(InputStreamReader reader) {
        StringBuilder sb = new StringBuilder();
        try {
            for (int ch; (ch = reader.read()) != -1; ) {
                sb.append((char) ch);
            }
            return sb.toString();
        } catch (IOException e) {
            return "";
        }

    }

}
