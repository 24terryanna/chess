package chess;

import java.util.Collection;

public class PawnMoveCalc implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
    // Implement the Pawn's moves; 1 forward if unoccupied, two forward on first turn, promotion if reaches end of board
        int pawnRow = position.getRow();
        int pawnColumn = position.getColumn();
        int[][] pawnDirection = {{1,0}};

        //check color, call helper function
        // if pawn is white -> white pawn moves
        // if pawn is black -> black pawn moves

        return null;
    }
     private PawnMoveCalc whitePawnMoves(ChessBoard board, ChessPosition position) {
        return null;
     }

    private PawnMoveCalc blackPawnMoves(ChessBoard board, ChessPosition position) {
        return null;
    }
    //private helper function for white pawn moves
    //private helped function for black pawn
}
