package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalc implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> pawnMoves = new ArrayList<>();

        ChessPiece currentPawn = board.getPiece(position);
        boolean isWhite = currentPawn.getTeamColor() == ChessGame.TeamColor.WHITE;

        if (isWhite) {
            whitePawnMoves(board, position, pawnMoves);
        } else {
            blackPawnMoves(board, position, pawnMoves);
        }
        return pawnMoves;
    }
     private void whitePawnMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> pawnMoves) {

         int pawnRow = position.getRow();
         int pawnColumn = position.getColumn();

         ChessPosition pawnForwardOne = new ChessPosition(pawnRow + 1, pawnColumn);
         if (withinBounds(pawnForwardOne) && board.getPiece(pawnForwardOne) == null) {
             if (pawnRow + 1 == 8) {
                 promotionPiece(pawnMoves, position, pawnForwardOne);
             } else {
                 pawnMoves.add(new ChessMove(position, pawnForwardOne, null));
             }
         }
         ChessPosition leftCapture = new ChessPosition(pawnRow + 1, pawnColumn - 1);
         if (withinBounds(leftCapture) && board.getPiece(leftCapture) != null && checkOpponentTeam(board, position, leftCapture)) {
             if (pawnRow + 1 == 8) {
                 promotionPiece(pawnMoves, position, leftCapture);
             } else {
                 pawnMoves.add(new ChessMove(position, leftCapture, null));
             }
         }
         ChessPosition rightCapture = new ChessPosition(pawnRow + 1, pawnColumn + 1);
         if (withinBounds(rightCapture) && board.getPiece(rightCapture) != null && checkOpponentTeam(board, position, rightCapture)) {
             if (pawnRow + 1 == 8) {
                 promotionPiece(pawnMoves, position, rightCapture);
             } else {
                 pawnMoves.add(new ChessMove(position, rightCapture, null));
             }
         }
         ChessPosition firstForwardTwo = new ChessPosition(pawnRow + 2, pawnColumn);
         if (pawnRow == 2 && board.getPiece(pawnForwardOne) == null && board.getPiece(firstForwardTwo) == null) {
             pawnMoves.add(new ChessMove(position, firstForwardTwo, null));
         }

     }

    private void blackPawnMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> pawnMoves) {
        int pawnRow = position.getRow();
        int pawnColumn = position.getColumn();

        ChessPosition pawnForwardOne = new ChessPosition(pawnRow - 1, pawnColumn);
        if (withinBounds(pawnForwardOne) && board.getPiece(pawnForwardOne) == null) {
            if (pawnRow - 1 == 1) {
                promotionPiece(pawnMoves, position, pawnForwardOne);
            } else {
                pawnMoves.add(new ChessMove(position, pawnForwardOne, null));
            }
        }

        ChessPosition leftCapture = new ChessPosition(pawnRow - 1, pawnColumn - 1);
        if (withinBounds(leftCapture) && board.getPiece(leftCapture) != null && checkOpponentTeam(board, position, leftCapture)) {
            if (pawnRow -1 == 1) {
                promotionPiece(pawnMoves, position, leftCapture);
            } else {
                pawnMoves.add(new ChessMove(position, leftCapture, null));
            }
        }

        ChessPosition rightCapture = new ChessPosition(pawnRow - 1, pawnColumn + 1);
        if (withinBounds(rightCapture) && board.getPiece(rightCapture) != null && checkOpponentTeam(board, position, rightCapture)) {
            if (pawnRow - 1 == 1) {
                promotionPiece(pawnMoves, position, rightCapture);
            } else {
                pawnMoves.add(new ChessMove(position, rightCapture, null));
            }
        }

        ChessPosition firstForwardTwo = new ChessPosition(pawnRow - 2, pawnColumn);
        if (pawnRow == 7 && board.getPiece(pawnForwardOne) == null && board.getPiece(firstForwardTwo) == null) {
            pawnMoves.add(new ChessMove(position, firstForwardTwo, null));
        }
    }


    private void promotionPiece(Collection<ChessMove> pawnMoves, ChessPosition pawnFrom, ChessPosition promotedTo) {
        pawnMoves.add(new ChessMove(pawnFrom, promotedTo, ChessPiece.PieceType.QUEEN));
        pawnMoves.add(new ChessMove(pawnFrom, promotedTo, ChessPiece.PieceType.ROOK));
        pawnMoves.add(new ChessMove(pawnFrom, promotedTo, ChessPiece.PieceType.BISHOP));
        pawnMoves.add(new ChessMove(pawnFrom, promotedTo, ChessPiece.PieceType.KNIGHT));
    }

}


