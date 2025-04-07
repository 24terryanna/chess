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

    private void handleCreate(String[] tokens) {
        if (tokens.length != 2) {
            System.out.println("Input: create <gameName>");
            return;
        }
        boolean success = server.createGame(tokens[1]);
        System.out.println(success ? "Game created." : "Failed to create game.");
    }

    private void handleList() {
        List<GameData> games = server.listGames();
        if (games.isEmpty()) {
            System.out.println("No games found.");
        } else {
            System.out.println("Games:");
            for (GameData game : games) {
                System.out.printf(" ID: %d | Name: %s | White: %s | Black: %s%n",
                        game.gameID(),
                        game.gameName(),
                        game.whiteUsername(),
                        game.blackUsername());
            }
        }
    }

    private void handleJoin(String[] tokens) {
        if (tokens.length != 3) {
            System.out.println("Input: join <gameID> <white|black>");
            return;
        }

        try {
            int gameID = Integer.parseInt(tokens[1]);
            String color = tokens[2].toLowerCase();
            if (!color.equals("white") && !color.equals("black")) {
                System.out.println("Invalid color. Choose 'white' or 'black'.");
                return;
            }
            boolean success = server.joinGame(gameID, color);
            if (success) {
                System.out.println(STR."Joined game as: \{color}!");
                return;
            } else {
                System.out.println("Could not join game.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid game ID.");
        }
    }

}
