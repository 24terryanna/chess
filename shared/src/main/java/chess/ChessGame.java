package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor currentTeam;

    public ChessGame() {
        this.board = new ChessBoard();
        this.currentTeam = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.currentTeam;
    }


    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentTeam = team;
    }


    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }


    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //check if valid move then check if it leaves king in check (don't include)
        ChessPiece piece = board.getPiece(startPosition);

        if (piece == null){
            return null;
        }

        Collection<ChessMove> moveOptions = board.getPiece(startPosition).pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();

        TeamColor team = piece.getTeamColor();
        //ChessPosition kingPosition = findKing(board, team);

        //check move outcome on temporary board; only add to valid move if not in check
        for (ChessMove move : moveOptions) {
            ChessBoard tempBoard = board.copy();

            board.addPiece(startPosition, null);
            board.addPiece(move.getEndPosition(), piece);

            if (!isInCheck(team, tempBoard)){     //not quite right?
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    private ChessPosition findKing(ChessBoard board, TeamColor teamColor) {
        ChessPosition kingPosition = null;
        for (int row = 1; row <= 8; row++) {    //is this iterating right?
            for (int col = 1; col <= 8; col++) {
                ChessPiece pieceAtPos = board.getPiece(new ChessPosition(row, col));
                if (pieceAtPos.getPieceType() == ChessPiece.PieceType.KING && pieceAtPos.getTeamColor() == teamColor) {
                    kingPosition = new ChessPosition(row, col);
                }
            }
        }
        return kingPosition;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //check if valid moves is empty
        Collection<ChessMove> allValidMoves = validMoves(move.getStartPosition());
        if (allValidMoves == null){
            throw new InvalidMoveException("No valid moves to play");
        }

        //check if move is valid
        boolean checkValidMoves = allValidMoves.contains(move);

        //check team color
        TeamColor team = board.getPiece(move.getStartPosition()).getTeamColor();
        boolean checkTeam = getTeamTurn() == team;

        if (checkValidMoves && checkTeam){
            ChessPiece movingPiece = board.getPiece(move.getStartPosition());

            board.addPiece(move.getStartPosition(), null);
            board.addPiece(move.getEndPosition(), movingPiece);

        } else {
            throw new InvalidMoveException("Not a valid move");
        }
    }

    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, board);
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor, ChessBoard chessBoard) {
        //get king position
        ChessPosition kingPosition = findKing(chessBoard, teamColor);
        if (kingPosition == null) {
            return false;
        }
        //check other team's piece moves
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition checkPos = new ChessPosition(row, col);
                ChessPiece pieceAtPos = chessBoard.getPiece(checkPos);

                //check opponent moves
                if (pieceAtPos != null && pieceAtPos.getTeamColor() != teamColor) {
                    Collection<ChessMove> oppMoves = pieceAtPos.pieceMoves(chessBoard, checkPos);

                    for (ChessMove move : oppMoves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //in Check; no move valid moves to get out
        //loop: clone board, get all piece moves (call getMoves method), apply moves one at a time,
        //then call isInCheck, iterate to next move
        return isInCheck(teamColor) && isInStalemate(teamColor);
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //if NOT in check and NO moves for any pieces -> true
        if (isInCheck(teamColor)) {
            return false;
        }

        //loop through each piece moves on the board
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++){
                ChessPosition checkPos = new ChessPosition(row, col);
                ChessPiece pieceAtPos = board.getPiece(checkPos);

                if (pieceAtPos != null && pieceAtPos.getTeamColor() == teamColor){
                    Collection<ChessMove> moves = validMoves(checkPos);

                    //check if moves is empty
                    if (moves != null){
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }


    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && currentTeam == chessGame.currentTeam;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currentTeam);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", currentTeam=" + currentTeam +
                '}';
    }
}


