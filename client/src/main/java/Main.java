import chess.*;
import client.ServerFacade;
import ui.PreLoginRepl;

public class Main {
    public static void main(String[] args) throws Exception {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println(STR."♕ 240 Chess Client: \{piece}");

        ServerFacade server = new ServerFacade();

        PreLoginRepl preLogin = new PreLoginRepl(server);
        preLogin.run();
        System.out.println("Exited");
    }
}