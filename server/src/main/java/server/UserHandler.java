package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import model.AuthData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import service.UserService;
import model.UserData;
//import spark.Request;
//import spark.Response;

public class UserHandler {
    UserService userService;

    public UserHandler() {
        this.userService = userService;
    }

    public Object register(Request request, Response response) throws BadRequestException{
        UserData userData = new Gson().fromJson(request.body(), UserData.class);

        if (userData.username() == null || userData.password() == null) {
            throw new BadRequestException("No username or password given");
        }

        try {
            AuthData authData = userService.createUser(userData);
            response.status(200);
            return new Gson().toJson(authData);
        } catch (BadRequestException ) {
            response.status(403);
            return "{ \"message\": \"Error: already taken\" }";
        }
    }



}
