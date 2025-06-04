package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class BoardPrinter {
    ChessGame game;

    public BoardPrinter(ChessGame game) {
        this.game = game;
    }

    public void printBoard(ChessGame.TeamColor perspective) {
        ChessBoard board = game.getBoard();
//        ChessGame.TeamColor perspective = game.getTeamTurn();

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
            System.out.print(row);
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

        System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "  ");
        for (char col : files) {
            System.out.print("   " + col + "   ");
        }
        System.out.print(RESET_TEXT_COLOR);
    }

    private String getSymbol(ChessPiece piece) {
        if (piece == null) { return EMPTY; }

        ChessGame.TeamColor color = piece.getTeamColor();
        ChessPiece.PieceType type = piece.getPieceType();

        return switch (type) {
            case KING -> color == ChessGame.TeamColor.WHITE ? WHITE_KING : BLACK_KING;
            case QUEEN -> color == ChessGame.TeamColor.WHITE ? WHITE_QUEEN : BLACK_QUEEN;
            case ROOK -> color == ChessGame.TeamColor.WHITE ? WHITE_ROOK : BLACK_ROOK;
            case BISHOP -> color == ChessGame.TeamColor.WHITE ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> color == ChessGame.TeamColor.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT;
            case PAWN -> color == ChessGame.TeamColor.WHITE ? WHITE_PAWN : BLACK_PAWN;
        };
    }


}
