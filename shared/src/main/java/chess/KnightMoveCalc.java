package chess;

import java.util.Collection;

public class KnightMoveCalc implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        // Implement the Knight's moves: 2 by 1, jump over pieces in between
        int knightRow = position.getRow();
        int knightColumn = position.getColumn();
        int[][] knightDirection = {{-2, 1}, {-2, -1 }, {-1, 2}, {1, 2}, {2, 1}, {2, -1}, {1, -2}, {-1, -2}};

        return null;
    }
}
