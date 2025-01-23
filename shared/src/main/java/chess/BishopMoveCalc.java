package chess;

import java.util.Collection;

public class BishopMoveCalc implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        // Implement the Bishop moves: move in diagonal lines as far as open space
        //start with 4 vectors for direction {top left, top right, bottom left, bottom right}
        int[][] directions = {{-1,-1}, {-1,1}, {1,-1}, {1,1}};

        return null; //holder
    }
}
