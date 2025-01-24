package chess;

import java.util.ArrayList;
import java.util.Collection;

// Implement the Bishop moves: diagonal lines as far as open space

public class BishopMoveCalc implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        int bishop_row = position.getRow();
        int bishop_column = position.getColumn();
        int[][] diagonals = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        //not sure what to do next

        return PieceMovesCalculator(board, position, diagonals, bishop_row, bishop_column);
    }
}
