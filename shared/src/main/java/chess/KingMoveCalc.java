package chess;

import java.util.Collection;

public class KingMoveCalc implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        // Implement the King's moves: 1 square any direction (pawn + bishop limited)
        int kingRow = position.getRow();
        int kingColumn = position.getColumn();
        int[][] kingDirection = {{-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}};

        return null; //PieceMovesCalculator...(board, p);
    }
}
