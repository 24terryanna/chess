package chess;

import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);

    //nested if statements to check if empty or off board

    default boolean withinBounds(ChessPosition position) {
        return (position.getRow() >= 0 && position.getRow() < 8
                && position.getColumn() >= 0 && position.getColumn() < 8);
    }
}
