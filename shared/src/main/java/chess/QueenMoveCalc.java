package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoveCalc implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> queenMoves = new ArrayList<ChessMove>();

        BishopMoveCalc bishopMoves = new BishopMoveCalc();
        RookMoveCalc rookMoves = new RookMoveCalc();

        queenMoves.addAll(bishopMoves.pieceMoves(board, position));
        queenMoves.addAll(rookMoves.pieceMoves(board, position));

        return queenMoves;
    }
}
