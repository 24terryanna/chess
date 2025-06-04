package ui;

import chess.ChessGame;
import client.ServerFacade;
import model.GameData;

import java.util.List;
import java.util.Scanner;

public class PostLoginRepl {

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
        int gameID = server.createGame(tokens[1]);
        if (gameID == -1) {
            System.out.println("Failed to create game.");
        } else {
            System.out.print("Game created with ID: " + gameID);
        }
    }

    private List<GameData> cachedGames = null;

    private void handleList() {
        cachedGames = server.listGames();
        if (cachedGames.isEmpty()) {
            System.out.println("No games found.");
        } else {
            System.out.println("Games:");
            int displayNumber = 1;
            for (GameData game : cachedGames) {
                System.out.printf(" ID: %d | Name: %s | White: %s | Black: %s%n",
                        displayNumber++,
                        game.gameName(),
                        game.whiteUsername() == null ? "-" : game.whiteUsername(),
                        game.blackUsername() == null ? "-" : game.blackUsername());
            }
        }

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
            //int gameID = Integer.parseInt(tokens[1]);
            int index = Integer.parseInt(tokens[1]) -1;
            if (cachedGames == null || index < 0 || index >= cachedGames.size()) {
                System.out.println("Invalid fame number. Use 'list' first.");
                return;
            }


            String color = tokens[2].toLowerCase();
            if (!color.equals("white") && !color.equals("black")) {
                System.out.println("Invalid color. Choose 'white' or 'black'.");
                return;
            }

            GameData selectedGame = cachedGames.get(index);
            boolean success = server.joinGame(selectedGame.gameID(), color);

            if (!success) {
                System.out.println("Failed to join game. That color may already be taken.");
                return;
            }

            System.out.println(("Joined game as: " + color + "!"));
            new GamePlayRepl(server, selectedGame.gameID(), selectedGame,
                    color.equals("white") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK).run();

        } catch (NumberFormatException e) {
            System.out.println("Invalid game number.");
        }
    }

    private void handleObserve(String[] tokens) {
        if (tokens.length != 2) {
            System.out.println("Input: observe <gameID>");
            return;
        }

        try {
            int index = Integer.parseInt(tokens[1]) - 1;
            if (cachedGames == null || index < 0 || index >= cachedGames.size()) {
                System.out.println("Invalid game number. Use 'list' first.");
                return;
            }

            GameData selectedGame = cachedGames.get(index);
            boolean success = server.joinGame(selectedGame.gameID(), "observer");

            if (success) {
                System.out.println(("Joined game as observer!"));
                new BoardPrinter(selectedGame.game()).printBoard(ChessGame.TeamColor.WHITE);
            } else {
                System.out.println("Failed to observe game.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid game number.");
        }
    }

    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println(" create <gameName>              Create a new game");
        System.out.println(" list games                     List all games");
        System.out.println(" join <gameID> <white|black>    Join a game as a player");
        System.out.println(" observe <gameID>               Observe a game");
        System.out.println(" logout                         Log out");
        System.out.println(" quit                           Quit the application");
    }

}
