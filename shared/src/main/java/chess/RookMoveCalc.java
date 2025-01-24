package chess;

import java.util.Collection;

public class RookMoveCalc implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        // Implement the Rook's moves: straight line as far as possible
        int rookRow = position.getRow();
        int rookColumn = position.getColumn();
        int[][] rookDirection = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        return null;
    }
}
