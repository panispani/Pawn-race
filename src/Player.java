/**
 * Created by Panayiotis on 03/01/2016.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {

    private Board board;
    private Game game;
    private Color playerColor;
    private Color opponentColor;
    private boolean isComputerPlayer;
    private Move whiteNextMove;
    private Move blackNextMove;

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
                if(board.getSquare(file - 1, rank + direction).occupiedBy() == opponentColor) {
                    to = board.getSquare(file - 1, rank + direction);
                    move = new Move(from, to, true, false);
                    moveList.add(move);
                }
            }

            if(file < 7) {
                if(board.getSquare(file + 1, rank + direction).occupiedBy() == opponentColor) {
                    to = board.getSquare(file + 1, rank + direction);
                    move = new Move(from, to, true, false);
                    moveList.add(move);
                }
            }

            //enPassantCapture

            Move lastMove = game.getLastMove();
            if(lastMove != null) {
                if(file > 0) {
                    if(board.getSquare(file - 1, rank).occupiedBy() == opponentColor &&
                            (board.getSquare(file - 1, rank + direction).occupiedBy() == Color.NONE) &&
                            GameUtil.moveIsEnPassant(lastMove) &&
                            (lastMove.getTo(). getX() == file - 1) &&
                            (lastMove.getTo().getY() == rank)) {
                        to = board.getSquare(file - 1, rank + direction);
                        move = new Move(from, to, true, true);
                        moveList.add(move);
                    }

                    if(file < 7) {
                        if (board.getSquare(file + 1, rank).occupiedBy() == opponentColor &&
                                (board.getSquare(file + 1, rank + direction).occupiedBy() == Color.NONE) &&
                                GameUtil.moveIsEnPassant(lastMove) &&
                                (lastMove.getTo().getX() == file + 1) &&
                                (lastMove.getTo().getY() == rank)) {
                            to = board.getSquare(file + 1, rank + direction);
                            move = new Move(from, to, true, true);
                            moveList.add(move);
                        }
                    }
                }
            }
        }
        GameUtil.printValidMoves(moveList.toArray(new Move[moveList.size()]));
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

    public void makeRandomMove(){ //random computerPlayer
        Move[] possibleMoves = getAllValidMoves();
        int numMoves = possibleMoves.length;
        int moveIndex = new Random().nextInt(numMoves);
        Move nextMove = possibleMoves[moveIndex];
        game.applyMove(nextMove);
    }

    public void makeMove(Player p1, Game game) { //computerPlayer
        minimax(0, p1.getPlayerColor(), Integer.MIN_VALUE, Integer.MAX_VALUE);

    }

    public int minimax(int level, Color player, int alpha, int beta) {
        //ending condition
        int nodeScore;
        Move[] moves = getAllValidMoves();

        if(moves.length == 0 || level == 20 || searchEndCondition()) {
            return scoreCalculation(player);//might need to also return a move
        }

        switch (player) {
            case WHITE:
                for(int i = 0; i < moves.length; i++) {
                    //apply move
                    game.applyMove(moves[i]);
                    nodeScore = minimax(level + 1, opponentColor, alpha, beta);
                    //unapply move
                    game.unapplyMove();
                    if(nodeScore > alpha) {
                        alpha = nodeScore;
                        //update move
                        this.whiteNextMove = moves[i];
                    }
                    if(alpha >= beta) break; //no-need to consider further
                }
                return alpha;
            case BLACK:
                for(int i = 0; i < moves.length; i++) {
                    game.applyMove(moves[i]);
                    nodeScore = minimax(level + 1, opponentColor, alpha, beta);
                    game.unapplyMove();
                    if(nodeScore < beta) {
                        beta = nodeScore;
                        //update move
                        this.blackNextMove = moves[i];
                    }
                    if(alpha >= beta) break; //no-need to consider further
                }
                return beta;

            default:
                assert(true):
                        "Player should be WHITE/BLACK";
                return Integer.MIN_VALUE;
        }
    }

    private int scoreCalculation(Color player) {

        int score = 0;

        //if draw return 0
       if(GameUtil.stealMate(board, player, game.getLastMove()))
           return 0;

        //if you won
            //return HIGH

       //if you lost
            //return LOW

       //spaceOfEachPawn 1 point

       //how many pawns can block each pawn (0, -1, -2)


       //doubled pawns 1 point

       //material difference 5

       //threatened - check if protected (-5)


       //if passedPawn
            //if minmoves to lose less
                //return HIGH
            //else
                //return LOw

       //else
            //if he passedPawn
                //if min to win less
                    //return HIGH
                //else
                    //return low


       return score;
    }

    private int infinite(Color player) {
        switch(player) {
            case WHITE:
                return Integer.MAX_VALUE;
            case BLACK:
                return Integer.MIN_VALUE;
            default:
                assert(true):
                        "Player should be Black/White";
                return 0;
        }
    }
    private boolean searchEndCondition() {
        //TODO if another end condition comes in mind
        return false;
    }
}
