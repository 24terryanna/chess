package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveCalc implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        int bishopRow = position.getRow();
        int bishopColumn = position.getColumn();
        int[][] bishopDirection = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        Collection<ChessMove> bishopMoves = new ArrayList<>();

        for (int[] dir : bishopDirection) {
            int newRow = bishopRow + dir[0];
            int newColumn = bishopColumn + dir[1] ;
            ChessPosition newPosition = new ChessPosition(newRow, newColumn);

            while (withinBounds(newPosition)) {
                if (board.getPiece(newPosition) == null) {
                    bishopMoves.add(new ChessMove(position, newPosition, null));
                } else {
                    if (checkOpponentTeam(board, position, newPosition)) {
                        bishopMoves.add(new ChessMove(position, newPosition, null));
                    }
                    break;
                }
                newRow += dir[0];
                newColumn += dir[1];
                newPosition = new ChessPosition(newRow, newColumn);

            }
        }
        return bishopMoves;
    }
}