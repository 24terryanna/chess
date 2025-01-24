package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalc implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        // Implement the Knight's moves: 2 by 1, jump over pieces in between

        Collection<ChessMove> knightMoves = new ArrayList<ChessMove>();
        int knightRow = position.getRow();
        int knightColumn = position.getColumn();
        int[][] knightDirection = {{-2, 1}, {-2, -1 }, {-1, 2}, {1, 2}, {2, 1}, {2, -1}, {1, -2}, {-1, -2}};

        for (int[] dir : knightDirection) {
            int newRow = knightRow + dir[0];
            int newColumn = knightColumn + dir[1];

            ChessPosition newPosition = new ChessPosition(newRow, newColumn);

            if (withinBounds(newPosition)) {
                //check if empty
                if (board.getPiece(newPosition) == null || checkOpponentTeam(board, position, newPosition)) {
                    knightMoves.add(new ChessMove(position, newPosition, null));
                }
            }
        }
        return knightMoves;
    }
}
