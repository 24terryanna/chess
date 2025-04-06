package server;

import com.sun.net.httpserver.HttpHandler;

import com.google.gson.Gson;


public class ServerFacade {

    private String serverUrl;
    private String authToken;

//    HttpCommunicator http;

//    public ServerFacade(String url) throws Exception {
//        this.serverUrl = url;
//        http = new HttpCommunicator(this, serverUrl);
//    }

    protected String getAuthToken() {
        return authToken;
    }

    protected void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

//    public boolean register(String username, String password, String email) {
//        return http.register(username, password, email);
//    }
}
