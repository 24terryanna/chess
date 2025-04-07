package client;



public class ServerFacade {

    private String domain;
    private String authToken;
    HttpCommunicator http;

    public ServerFacade() throws Exception {
        this("localhost:8080");
    }

    public ServerFacade(String url) throws Exception {
        this.domain = url;
        http = new HttpCommunicator(this, domain);
    }

    String getAuthToken() {
        return authToken;
    }

    void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public boolean register(String username, String password, String email) {
        return http.register(username, password, email);
    }

    public boolean login(String username, String password) {
        return http.login(username, password);
    }

    public boolean logout() {
        return http.logout();
    }

    public boolean createGame(String gameName) {
        return http.createGame(gameName);
    }


}
