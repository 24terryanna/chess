package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveCalc implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        // Implement the King's moves: 1 square any direction (pawn + bishop limited)

        Collection<ChessMove> kingMoves = new ArrayList<>();
        int kingRow = position.getRow();
        int kingColumn = position.getColumn();
        int[][] kingDirection = {{-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}};

        for (int[] dir : kingDirection) {
            int newRow = kingRow + dir[0];
            int newColumn = kingColumn + dir[1];

            ChessPosition newPosition = new ChessPosition(newRow, newColumn);

            if (withinBounds(newPosition)) {
                //check if empty
                if (board.getPiece(newPosition) == null || checkOpponentTeam(board, position, newPosition)) {
                    kingMoves.add(new ChessMove(position, newPosition, null));
                }
            }
        }
        return kingMoves;
    }
}
