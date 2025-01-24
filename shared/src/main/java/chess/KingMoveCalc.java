package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveCalc implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        // Implement the King's moves: 1 square any direction (pawn + bishop limited)
        //Data Structure to store chess moves
        Collection<ChessMove> kingMoves = new ArrayList<ChessMove>();
        int kingRow = position.getRow();
        int kingColumn = position.getColumn();
        int[][] kingDirection = {{-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}};


        //NewRow NewPosition
        new ChessMove(position, newPosition, )
        return kingMoves; //PieceMovesCalculator...(board, p);
    }
}
