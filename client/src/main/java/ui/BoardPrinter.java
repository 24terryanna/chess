package ui;

import chess.ChessBoard;
import chess.ChessGame;

import static ui.EscapeSequences.*;

public class BoardPrinter {
    public void printBoard(ChessBoard board, ChessGame.TeamColor perspective) {
        System.out.println(ERASE_SCREEN);

        int startRow;
        int endRow;
        int step;

        if  (perspective == ChessGame.TeamColor.WHITE) {
            startRow = 8;
            endRow = 0;
            step = -1;
        } else {
            startRow = 1;
            endRow = 9;
            step = 1;
        }

        char[] files;
        if (perspective == ChessGame.TeamColor.WHITE) {
            files = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        } else {
            files = new char[]{'h', 'g', 'f', 'e', 'd', 'c', 'b', 'a'};
        }
    }


}
