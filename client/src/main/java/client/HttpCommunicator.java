package client;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import model.GameData;
import model.GamesList;
import ui.BoardPrinter;

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

    public boolean clearDatabase() {
        Map<String, Object> response = request("DELETE", "/db", null);
        return !response.containsKey("Error");
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

    public int createGame(String gameName) {
        var body = Map.of("gameName", gameName);
        var jsonBody = new Gson().toJson(body);
        Map response = request("GET", "/game", jsonBody);

        if (response.containsKey("Error")) {
            return -1;
        }

        double gameID = (double) response.get("gameID");
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
        Map<String, Object> body = playerColor != null ?
                Map.of("gameID", gameID, "playerColor", playerColor) :
                Map.of("gameID", gameID);

        var jsonBody = new Gson().toJson(body);
        Map response = request("PUT", "/game", jsonBody);
        return !response.containsKey("Error");
    }

    public boolean makeMove(int gameID, ChessMove move) {
        var body = Map.of("gameID", gameID, "move", move);
        var jsonBody = new Gson().toJson(body);
        Map response = request("PUT", "/game/" +gameID, jsonBody);
        return !response.containsKey("Error");
    }

    public boolean leaveGame(int gameID) {
        var body = Map.of("gameID", gameID);
        var jsonBody = new Gson().toJson(body);
        Map response = request("PUT", "/game/" + gameID, jsonBody);
        return !response.containsKey("Error");
    }

    private String requestString(String method, String endpoint) {
        return requestString(method, endpoint, null);
    }

    private String requestString(String method, String endpoint, String body) {
        try {
            HttpURLConnection connection = makeConnection(method, endpoint, body);
            int statusCode = connection.getResponseCode();

            InputStream inputStream;
            if (statusCode >= 200 && statusCode < 300) {
                inputStream  = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }

            if (inputStream == null) {
                return "Error: Empty response body (HTTP status " + statusCode;
            }

            try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                String response = readerToString(reader);
                if (statusCode >= 200 && statusCode < 300) {
                    return response;
                } else {
                    return "Error: " + response;
                }
            }
        } catch (IOException | URISyntaxException e) {
            return "Error: " + e.getMessage();
        }
    }

    private Map<String, Object> request(String method, String endpoint) {
        return request(method, endpoint, null);
    }

    private Map<String, Object> request(String method, String endpoint, String body) {
        try {
            String response = requestString(method, endpoint, body);

            if (response == null || !response.trim().startsWith("{")) {
                return Map.of("Error", response);
            }

            Map<String, Object> responseMap = new Gson().fromJson(response, Map.class);
            //return new Gson().fromJson(response, Map.class);

            if (responseMap.containsKey("message") && ((String) responseMap.get("message")).startsWith("Error")) {
                return Map.of("Error", responseMap.get("message"));
            }

            return responseMap;

        } catch (Exception e) {
            return Map.of("Error", "Failed to parse response: " + e.getMessage());
        }

    }


    private HttpURLConnection makeConnection(String method, String endpoint, String body)
            throws URISyntaxException, IOException {

        URI uri = new URI(url + endpoint);
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
