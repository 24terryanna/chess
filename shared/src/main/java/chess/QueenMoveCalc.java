package chess;

import java.util.Collection;

public class QueenMoveCalc implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        // Implement the Queen's moves: bishop + rook
        int queenRow = position.getRow();
        int queenColumn = position.getColumn();
        int[][] queenDirection = {{-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}};


        return null;
    }
    //call rook and bishop; add both to queen collection and return
}
