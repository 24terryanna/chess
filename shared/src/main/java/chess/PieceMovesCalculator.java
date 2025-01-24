package chess;

import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);

    //general out-of-bounds code?
    default boolean withinBounds(ChessPosition position) {
        return position.getRow() >= 0;
    }

    // general occupied code?
}
