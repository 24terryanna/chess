package chess;

import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);

    //nested if statements to check if empty or off board, check team color (if same, no move; if opposite, valid move)

    default boolean withinBounds(ChessPosition position) {
        return (position.getRow() >= 1 && position.getRow() < 9
                && position.getColumn() >= 1 && position.getColumn() < 9);
    }

    default boolean checkOpponentTeam(ChessBoard board, ChessPosition position, ChessPosition newPosition) {
        ChessPiece currentPiece = board.getPiece(position);
        ChessPiece unknownPiece = board.getPiece(newPosition);

        return currentPiece.getTeamColor() != unknownPiece.getTeamColor();
    }
}
