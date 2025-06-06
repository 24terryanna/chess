import chess.ChessGame;
import chess.ChessPiece;
import client.ServerFacade;
import ui.PreLoginRepl;

public class Main {
    public static void main(String[] args) throws Exception {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        ServerFacade server = new ServerFacade();

        while (true) {
            PreLoginRepl preLogin = new PreLoginRepl(server);
            boolean continueRunning = preLogin.run();
            if (!continueRunning) {
                System.out.println("Exited");
                break;
            }
        }
    }
}