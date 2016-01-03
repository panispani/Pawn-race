/**
 * Created by Panayiotis on 03/01/2016.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {

    Board board;
    Game game;
    Color playerColor;
    Color opponentColor;
    boolean isComputerPlayer;

    public Player(Board board, Game game, Color color, boolean isComputerPlayer) {
        this.board         = board;
        this.game          = game;
        this.playerColor   = color;
        setOpponent(color);
        this.isComputerPlayer    = isComputerPlayer;
    }

    public void setOpponent(Color color) {
        this.opponentColor = GameUtil.oppositePlayer(color);
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public boolean isComputerPlayer() {
        return isComputerPlayer;
    }

    public Square[] getAllPawns() {
        //array of player pawns
        List<Square> pawnList = new ArrayList<Square>();
        for(int file = 0; file < 8; file++) {
            for(int rank = 0; rank < 8; rank++) {
                if(board.getSquare(file, rank).occupiedBy() == playerColor)
                    pawnList.add(board.getSquare(file, rank));
            }
        }
        return pawnList.toArray(new Square[pawnList.size()]);
    }

    public Move[] getAllValidMoves() {
        Square[] pawnList = getAllPawns();
        List<Move> moveList = new ArrayList<Move>();
        for(int i = 0; i < pawnList.length; i++) {
            Square from = pawnList[i];
            int file = from.getX();
            int rank = from.getY();
            Square to;
            Move move;
            int direction = GameUtil.getDirectionOfPawn(playerColor);
            //normal move
            if(board.getSquare(file, rank + direction).occupiedBy() == Color.NONE){
                to = board.getSquare(file, rank + direction);
                move = new Move(from, to, false, false);
                moveList.add(move);
            }

            //enPassant
            if(!GameUtil.haveMovedPawn(rank, playerColor) &&
                    (board.getSquare(file, rank + direction).occupiedBy() == Color.NONE) &&
                    (board.getSquare(file, rank + 2 * direction).occupiedBy() == Color.NONE)){
                to = board.getSquare(file, rank + 2 * direction);
                move = new Move(from, to, false, false);
                moveList.add(move);
            }

            //capture
            if(file > 0) {
                if(board.getSquare(file - 1, rank - direction).occupiedBy() == opponentColor) {
                    to = board.getSquare(file - 1, rank - direction);
                    move = new Move(from, to, true, false);
                }
            }

            if(file < 7) {
                if(board.getSquare(file + 1, rank - direction).occupiedBy() == opponentColor) {
                    to = board.getSquare(file + 1, rank - direction);
                    move = new Move(from, to, true, false);
                }
            }

            //enPassantCapture

            Move lastMove = game.getLastMove();
            if(lastMove != null) {
                if(file > 0) {
                    if(board.getSquare(file - 1, rank).occupiedBy() == opponentColor &&
                            (board.getSquare(file - 1, rank - direction).occupiedBy() == Color.NONE) &&
                            GameUtil.moveIsEnPassant(lastMove) &&
                            (lastMove.getTo(). getX() == file - 1) &&
                            (lastMove.getTo().getY() == rank)) {
                        to = board.getSquare(file - 1, rank + direction);
                        move = new Move(from, to, true, true);
                    }

                    if(file < 7) {
                        if (board.getSquare(file + 1, rank).occupiedBy() == opponentColor &&
                                (board.getSquare(file + 1, rank - direction).occupiedBy() == Color.NONE) &&
                                GameUtil.moveIsEnPassant(lastMove) &&
                                (lastMove.getTo().getX() == file + 1) &&
                                (lastMove.getTo().getY() == rank)) {
                            to = board.getSquare(file + 1, rank + direction);
                            move = new Move(from, to, true, true);
                        }
                    }
                }
            }
        }
        return moveList.toArray(new Move[moveList.size()]);
    }

    public boolean isPassedPawn(Square square) {
        //no pawn can stop it from promoting
        for(int rank = square.getY() + 1; rank < 8; rank++)
            if(board.getSquare(square.getX(), rank).occupiedBy() != Color.NONE)
                return false;

        if(square.getX() > 0){
            for(int rank = square.getY() + 1; rank < 8; rank++)
                if(board.getSquare(square.getX() - 1, rank).occupiedBy() == Color.BLACK)
                    return false;
        }

        if(square.getX() < 7){
            for(int rank = square.getY() + 1; rank < 8; rank++)
                if(board.getSquare(square.getX() + 1, rank).occupiedBy() == Color.BLACK)
                    return false;
        }

        return true;

    }

    public void makeMove(){ //computerPlayer logic, random for now
        Move[] possibleMoves = getAllValidMoves();
        int numMoves = possibleMoves.length;
        int moveIndex = new Random().nextInt(numMoves);
        Move nextMove = possibleMoves[moveIndex];
        game.applyMove(nextMove);
    }
}
