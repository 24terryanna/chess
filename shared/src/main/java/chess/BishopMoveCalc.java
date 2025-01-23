package chess;

import java.util.ArrayList;
import java.util.Collection;

// Implement the Bishop moves: diagonal lines as far as open space

public class BishopMoveCalc implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        //4 vectors for direction options {top left, top right, bottom left, bottom right}
        int[][] diagonals = {{-1,-1}, {-1,1}, {1,-1}, {1,1}};

        for (int[] diagonal : diagonals) {
            int rowDirection = diagonal[0];
            int colDirection = diagonal[1];
            int currentRow = position.getRow();
            int currentCol = position.getColumn();

            //move one in current direction until occupied or out of bounds --> loop
            while (true) {
                currentRow += rowDirection;
                currentCol += colDirection;
                //check bounds & occupied -> conditionals

            }
        }

        return possibleMoves;
    }
}
