package client;

import org.junit.jupiter.api.*;

import server.Server;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() throws Exception {
        server = new Server();
        var port = server.run(0);
        System.out.println(STR."Started test HTTP server on \{port}");
        facade = new ServerFacade(STR."localhost:\{port}");

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void registerSuccess() {
        assertTrue(facade.register("user1", "pass123", "user1@email.com"));
    }

    @Test
    void registerDuplicateFails() {
        facade.register("user2", "pass123", "user2@email.com");
        assertFalse(facade.register("user2", "pass123", "user2@email.com"));
    }

}
