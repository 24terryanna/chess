package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalc implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        Collection<ChessMove> knightMoves = new ArrayList<>();
        int knightRow = position.getRow();
        int knightColumn = position.getColumn();
        int[][] knightDirection = {{-2, 1}, {-2, -1 }, {-1, 2}, {1, 2}, {2, 1}, {2, -1}, {1, -2}, {-1, -2}};

        for (int[] dir : knightDirection) {
            int newRow = knightRow + dir[0];
            int newColumn = knightColumn + dir[1];

            ChessPosition newPosition = new ChessPosition(newRow, newColumn);

            if (withinBounds(newPosition)) {
                if (board.getPiece(newPosition) == null || checkOpponentTeam(board, position, newPosition)) {
                    knightMoves.add(new ChessMove(position, newPosition, null));
                }
            }
        }
        return knightMoves;
    }
}
