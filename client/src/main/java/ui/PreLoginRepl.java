package ui;
import client.ServerFacade;
import java.util.Scanner;

public class PreLoginRepl {
    //register, login, quit

    private final Scanner scanner = new Scanner(System.in);
    private final ServerFacade server;

    public PreLoginRepl(ServerFacade server){
        this.server = server;
    }

    public void run() {
        System.out.println("Welcome to Chess! Type 'help' for commands.");
        while(true) {
            System.out.print(">>> ");
            String input = scanner.nextLine().trim();
            String[] tokens = input.split("\\s+");
            String command = tokens[0].toLowerCase();

            switch(command) {
                case "register" -> handleResiter(tokens);
                case "login" -> handleLogin(tokens);
                case "help" -> printHelp();
                case "quit" -> {
                    System.out.println("Quit successful");
                    return;
                }
                default -> System.out.println("Unknown command. Type 'help' for options.");
            }
        }
    }

}
