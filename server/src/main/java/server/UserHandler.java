package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import request.RegisterRequest;
import request.RegisterResult;
import service.UserService;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


public class UserHandler implements HttpHandler{
    private final UserService userService;
    private final Gson gson = new Gson();

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, 0);
            return;
        }

        //read request body
        InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        RegisterRequest request = gson.fromJson((inputStreamReader, RegisterRequest.class));

        //call service
        RegisterResult result = userService.register(request);

        //send response
        String response = gson.toJson(result);
        exchange.sendResponseHeaders(result.statusCode(), response.length());
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        outputStream.close();

    }
}
