package chess;

import java.util.ArrayList;
import java.util.Collection;

// Implement the Bishop moves: diagonal lines as far as open space

public class BishopMoveCalc implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        int bishopRow = position.getRow();
        int bishopColumn = position.getColumn();
        int[][] bishopDirection = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        //not sure what to do next

        return null; //PieceMovesCalculator...(board, position, bishopDirection, bishopRow, bishopColumn);
    }
}
