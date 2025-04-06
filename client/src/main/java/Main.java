import chess.*;
import server.ServerFacade;
import ui.PreLoginRepl;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        ServerFacade server = new ServerFacade();

        PreLoginRepl preLogin = new PreLoginRepl(server);
        preLogin.run();
        System.out.println("Exited");
    }
}