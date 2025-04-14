package ui;

import chess.ChessGame;
import client.ServerFacade;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;

import java.util.Scanner;

public class GamePlayRepl {

    private final Scanner scanner;
    private final ServerFacade server;
    private final int gameID;
    private final ChessGame.TeamColor perspective;
    private final ChessGame game;
    public static BoardPrinter boardPrinter;

    public GamePlayRepl(ServerFacade server, int gameID, GameData gameData, ChessGame.TeamColor perspective) {
        this.server = server;
        this.gameID = gameID;
        this.perspective = perspective;
        this.scanner = new Scanner(System.in);
        this.game = gameData.game();

        boardPrinter = new BoardPrinter(game);
    }

    public void run() {
        System.out.println("You've entered the game. Type 'help' to see commands.");
        boolean inGame = true;

        while (inGame) {
            System.out.println("[gameplay] >>> ");
            String input = scanner.nextLine().trim();
            String[] parts = input.split("\\s+");

            switch (parts[0].toLowerCase()) {
                case "move" -> {
                    if (parts.length != 3) {
                        System.out.println("Input: move <start> <end>");
                    } else {
                        ChessPosition from = parsePosition(parts[1]);
                        ChessPosition to = parsePosition(parts[2]);
                        if (from != null && to != null) {
                            server.makeMove(gameID, new ChessMove(from, to , null));
                        } else {
                            System.out.println("Invalid position format.");
                        }
                    }
                }

                case "resign" -> {
                    server.resignGame(gameID);
                    inGame = false;
                }

                case "leave" -> {
                    server.leaveGame(gameID);
                    inGame = false;
                }

                case "show" -> {
                    server.showBoard(gameID);
                }

                case "help" -> showHelp();
                default -> System.out.println("Unknown command. Type 'help'.");
            }
        }
    }

    private void showHelp() {
        System.out.println("""
                Commands:
                - move <start> <end>   Make a move (e.g. move e2 e4)
                - resign               Resign the game
                - leave                Leave the game
                - show                 Show current board
                - help                 Show this help menu
                """);
    }

    private ChessPosition parsePosition(String position) {
        if (position.length() != 2) return null;
        char col = position.charAt(0);
        int row = Character.getNumericValue(position.charAt(1));
        return new ChessPosition(row, col - 'a' + 1);
    }

}
