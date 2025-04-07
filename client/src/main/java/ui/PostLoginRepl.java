package ui;

import client.ServerFacade;
import model.GameData;

import java.util.List;
import java.util.Scanner;

public class PostLoginRepl {
    //createGame, listGames, joinGame, logout

    private final Scanner scanner = new Scanner(System.in);
    private final ServerFacade server;

    public PostLoginRepl(ServerFacade server) {
        this.server = server;
    }

    public void run() {
        System.out.println("You are now logged in. Type 'help' for commands.");
        while (true) {
            System.out.print(">>> ");
            String input = scanner.nextLine().trim();
            String[] tokens = input.split("\\s+");
            String command = tokens[0].toLowerCase();

            switch (command) {
                case "create" -> handleCreate(tokens);
                case "list" -> handleList();
                case "join" -> handleJoin(tokens);
                case "observe" -> handleObserve(tokens);
                case "logout" -> {
                    server.logout();
                    System.out.println("Logged out.");
                    return;
                }
                case "help" -> printHelp();
                case "quit" -> {
                    System.out.println("Quit");
                    System.exit(0);
                }
                default -> System.out.println("Unknown command. Type 'help' for a list.");
            }
        }
    }

}
