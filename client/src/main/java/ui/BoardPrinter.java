package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

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

        for (int row = startRow; row != endRow; row += step) {
            System.out.print(" " + row + " ");
            for (char col : files) {
                int colNum = col - 'a' + 1;
                ChessPosition position = new ChessPosition(row, colNum);
                ChessPiece piece = board.getPiece(position);

                boolean isLightSquare = (row + colNum) % 2 == 0;
                String bgColor;
                if (isLightSquare) {
                    bgColor = SET_BG_COLOR_LIGHT_GREY;
                } else {
                    bgColor = SET_BG_COLOR_DARK_GREEN;
                }

                String display;
                if (piece == null) {
                    display = EMPTY;
                } else {
                    display = getSymbol(piece);
                }

                System.out.print(bgColor + display + RESET_BG_COLOR + RESET_TEXT_COLOR);
            }
            System.out.println();
        }

        System.out.print("  ");
        for (char col : files) {
            System.out.print(" " + col + " ");
        }
        System.out.println();
    }

    private String getSymbol(ChessPiece piece) {
        ChessGame.TeamColor color = piece.getTeamColor();
        ChessPiece.PieceType type = piece.getPieceType();

        if (color == ChessGame.TeamColor.WHITE) {
            if (type == ChessPiece.PieceType.KING) return WHITE_KING;
            if (type == ChessPiece.PieceType.QUEEN) return WHITE_QUEEN;
            if (type == ChessPiece.PieceType.BISHOP) return WHITE_BISHOP;
            if (type == ChessPiece.PieceType.KNIGHT) return WHITE_KNIGHT;
            if (type == ChessPiece.PieceType.ROOK) return WHITE_ROOK;
            if (type == ChessPiece.PieceType.PAWN) return WHITE_PAWN;
        }
    }


}
