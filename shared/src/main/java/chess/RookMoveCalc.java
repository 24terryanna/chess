package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalc implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        int rookRow = position.getRow();
        int rookColumn = position.getColumn();
        int[][] rookDirection = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        Collection<ChessMove> rookMoves = new ArrayList<>();

        for (int[] dir : rookDirection) {
            int newRow = rookRow + dir[0];
            int newColumn = rookColumn + dir[1] ;
            ChessPosition newPosition = new ChessPosition(newRow, newColumn);

            while (withinBounds(newPosition)) {
                if (board.getPiece(newPosition) == null) {
                    rookMoves.add(new ChessMove(position, newPosition, null));
                } else {
                    if (checkOpponentTeam(board, position, newPosition)) {
                        rookMoves.add(new ChessMove(position, newPosition, null));
                    }
                    break;
                }
                newRow += dir[0];
                newColumn += dir[1];
                newPosition = new ChessPosition(newRow, newColumn);

            }
        }
        return rookMoves;
    }
}
