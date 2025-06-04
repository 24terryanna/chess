package ui;
import client.ServerFacade;
import java.util.Scanner;

public class PreLoginRepl {

    private final Scanner scanner = new Scanner(System.in);
    private final ServerFacade server;

    public PreLoginRepl(ServerFacade server){
        this.server = server;
    }

    public boolean run() {
        System.out.println("Welcome to Chess! Type 'help' for commands.");
        while(true) {
            System.out.print(">>> ");
            String input = scanner.nextLine().trim();
            String[] tokens = input.split("\\s+");
            String command = tokens[0].toLowerCase();

            switch(command) {
                case "register" -> {
                    if (handleRegister(tokens)) {
                        new PostLoginRepl(server).run();
                        return true;
                    };
                }
                case "login" -> {
                    if (handleLogin(tokens)) {
                        new PostLoginRepl(server).run();
                        return true;
                    }
                }
                case "help" -> printHelp();
                case "quit" -> {
                    System.out.println("Quit successful");
                    return false;
                }
                default -> System.out.println("Unknown command. Type 'help' for options.");
            }
        }
    }

    private boolean handleRegister(String[] tokens) {
        if (tokens.length != 4) {
            System.out.println("Input: register <username> <password> <email>");
            return false;
        }
        String username = tokens[1];
        String password = tokens[2];
        String email = tokens[3];

        boolean registered = server.register(username, password, email);
        if (!registered) {
            System.out.println("Registration failed.");
            return false;
        }

        System.out.println("Successful registration!");
        return true;

//        boolean loggenIn = server.login(username, password);
//        if (loggenIn) {
//            System.out.println("Logged in!");
//            new PostLoginRepl(server).run();
//        } else {
//            System.out.println("Login failed after registration.");
//        }
    }

    private boolean handleLogin(String[] tokens) {
        if (tokens.length != 3) {
            System.out.println("Input: login <username> <password>");
            return false;
        }
        boolean success = server.login(tokens[1], tokens[2]);
        if (success){
            System.out.println("Logged in!");
            new PostLoginRepl(server).run();
        } else {
            System.out.println("Login failed.");
            return false;
        }
        // this might be incorrect
        return false;
    }

    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println(" register <username> <password> <email>");
        System.out.println(" login <username> <password>");
        System.out.println(" quit");
    }

}
