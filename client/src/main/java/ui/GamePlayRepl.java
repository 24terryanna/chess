package ui;

import client.ServerFacade;
import chess.ChessMove;
import chess.ChessPosition;

import javax.sound.midi.SysexMessage;
import javax.swing.text.ChangedCharSetException;
import java.util.Scanner;

public class GamePlayRepl {
    //makeMove, resign, leave, observe

    private final Scanner scanner;
    private final ServerFacade server;
    private final int gameID;

    public GamePlayRepl(ServerFacade server, int gameID) {
        this.server = server;
        this.gameID = gameID;
        this.scanner = new Scanner(System.in);
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
}
