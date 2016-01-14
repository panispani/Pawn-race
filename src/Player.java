/**
 * Created by Panayiotis on 03/01/2016.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Player {

    private Board board;
    private Game game;
    private Color playerColor;
    private Color opponentColor;
    private boolean isComputerPlayer;
    private Move whiteNextMove;
    private Move blackNextMove;
    private final int MATERIAL = 10;
    private int movesDone = 0;

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

    public Square[] getAllPawns(Color player) {
        //array of player pawns
        List<Square> pawnList = new ArrayList<Square>();
        for(int file = 0; file < 8; file++) {
            for(int rank = 0; rank < 8; rank++) {
                if(board.getSquare(file, rank).occupiedBy() == player)
                    pawnList.add(board.getSquare(file, rank));
            }
        }
        return pawnList.toArray(new Square[pawnList.size()]);
    }

    public Move[] getAllValidMoves(Color player) {

        Color opponent = GameUtil.oppositePlayer(player);

        Square[] pawnList = getAllPawns(player);
        List<Move> moveList = new ArrayList<Move>();
        for(int i = 0; i < pawnList.length; i++) {
            Square from = pawnList[i];
            int file = from.getX();
            int rank = from.getY();
            Square to;
            Move move;
            int direction = GameUtil.getDirectionOfPawn(player);
            //normal move
            if(board.getSquare(file, rank + direction).occupiedBy() == Color.NONE){
                to = board.getSquare(file, rank + direction);
                move = new Move(from, to, false, false);
                moveList.add(move);
            }

            //enPassant
            if(!GameUtil.haveMovedPawn(rank, player) &&
                    (board.getSquare(file, rank + direction).occupiedBy() == Color.NONE) &&
                    (board.getSquare(file, rank + 2 * direction).occupiedBy() == Color.NONE)){
                to = board.getSquare(file, rank + 2 * direction);
                move = new Move(from, to, false, false);
                moveList.add(move);
            }

            //capture
            if(file > 0) {
                if(board.getSquare(file - 1, rank + direction).occupiedBy() == opponent) {
                    to = board.getSquare(file - 1, rank + direction);
                    move = new Move(from, to, true, false);
                    moveList.add(move);
                }
            }

            if(file < 7) {
                if(board.getSquare(file + 1, rank + direction).occupiedBy() == opponent) {
                    to = board.getSquare(file + 1, rank + direction);
                    move = new Move(from, to, true, false);
                    moveList.add(move);
                }
            }

            //enPassantCapture
            Move lastMove = game.getLastMove();
            if(lastMove != null) {
                if(file > 0) {
                    if(board.getSquare(file - 1, rank).occupiedBy() == opponent &&
                            (board.getSquare(file - 1, rank + direction).occupiedBy() == Color.NONE) &&
                            GameUtil.moveIsEnPassant(lastMove) &&
                            (lastMove.getTo(). getX() == file - 1) &&
                            (lastMove.getTo().getY() == rank)) {
                        to = board.getSquare(file - 1, rank + direction);
                        move = new Move(from, to, true, true);
                        moveList.add(move);
                    }

                    if(file < 7) {
                        if (board.getSquare(file + 1, rank).occupiedBy() == opponent &&
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
        //GameUtil.printValidMoves(moveList.toArray(new Move[moveList.size()]));
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
        Move[] possibleMoves = getAllValidMoves(playerColor);
        int numMoves = possibleMoves.length;
        int moveIndex = new Random().nextInt(numMoves);
        Move nextMove = possibleMoves[moveIndex];
        game.applyMove(nextMove);
    }

    private void whiteOpeningRepertoire() {
        //promote adjacent pawns to his gap
        
    }

    private void blackOpeningRepertoire() {
        //promote pawns adjacent to his gap
        //if gap is a/h setup the win
        if(board.getWhiteGap() == 'A' || board.getWhiteGap() == 'H') {
            if(game.getLastMove().getTo().getX() == 1 && game.getLastMove().getTo().getY() == 4) {
                //b3
            } else {
                //move list to choose from is capture, b4 c4 a4
            }

        } else {
            //just play move adjacent to gap
        }

    }

    public void makeMove() { //computerPlayer

        if (movesDone < 3) {
            switch(playerColor) {
                case WHITE:
                    whiteOpeningRepertoire();
                    break;
                case BLACK:
                    blackOpeningRepertoire();
                    break;
                default:
                    assert(false):
                            "Player should be Black/White";
                    break;
            }
            //movesDone++;
            //return;
        }
        movesDone++;
        minimax(0, playerColor, Integer.MIN_VALUE, Integer.MAX_VALUE);
        switch (playerColor) {
            case WHITE:
                game.applyMove(this.whiteNextMove);
                break;
            case BLACK:
                game.applyMove(this.blackNextMove);
                break;
            default:
                assert(false) :
                        "Player should be BLACK/WHITE";
                break;
        }
    }

    List<Square> whitePassedPawnList = new ArrayList<Square>();
    List<Square> blackPassedPawnList = new ArrayList<Square>();

    public int minimax(int level, Color player, int alpha, int beta) {
        //ending condition
        int nodeScore;
        Color opponent = GameUtil.oppositePlayer(player);
        Move[] moves = getAllValidMoves(player);

        if(game.isFinished() || moves.length == 0 || level == 7 || searchEndCondition()) {
            return scoreCalculation(player);
        }
        //revise alpha beta pruning
        switch (player) {
            case WHITE:
                for(int i = 0; i < moves.length; i++) {
                    //apply move
                    game.applyMove(moves[i]);

                    //not calculated everytime, passed from parent to node - time efficient
                    boolean addedPawn = false;
                    if(isPassedPawn(moves[i].getTo()) && !isPassedPawn(moves[i].getFrom())) {
                        addedPawn = true;
                        whitePassedPawnList.add(moves[i].getTo());
                    }
                    nodeScore = minimax(level + 1, opponent, alpha, beta);
                    if(addedPawn) {
                        whitePassedPawnList.remove(whitePassedPawnList.size() - 1);
                    }

                    //unapply move
                    game.unapplyMove();
                    if(nodeScore > alpha) {
                        alpha = nodeScore;
                        //update move
                        if(level == 0)
                        this.whiteNextMove = moves[i];
                    }
                    if(alpha >= beta) break; //no-need to consider further
                }
                return alpha;
            case BLACK:
                for(int i = 0; i < moves.length; i++) {
                    game.applyMove(moves[i]);

                    boolean addedPawn = false;
                    if(isPassedPawn(moves[i].getTo()) && !isPassedPawn(moves[i].getFrom())) {
                        addedPawn = true;
                        blackPassedPawnList.add(moves[i].getTo());
                    }
                    nodeScore = minimax(level + 1, opponent, alpha, beta);
                    if(addedPawn) {
                        blackPassedPawnList.remove(blackPassedPawnList.size() - 1);
                    }

                    game.unapplyMove();

                    if(nodeScore < beta) {
                        beta = nodeScore;
                        //update move
                        //Take in account that shorter wins are better..
                        if(level == 0)
                        this.blackNextMove = moves[i];
                    }
                    if(alpha >= beta) break; //no-need to consider further
                }
                return beta;

            default:
                assert(false):
                        "Player should be WHITE/BLACK";
                return Integer.MIN_VALUE;
        }
    }

    private int scoreCalculation(Color player) {
        //if draw return 0
        if(GameUtil.stealMate(board, player, game.getLastMove()))
           return 0;

        //if won/lost
        if(game.isFinished()) {
           Color winner = game.getGameResult();
           return infinite(winner);
        }

        int score = 0;
        //default false?
        int whiteSupportFile[] = new int[8];
        int blackSupportFile[] = new int[8];
        int whiteSpace[] = new int[8];
        int blackSpace[] = new int[8];
        boolean whiteFile[] = new boolean[8];
        boolean blackFile[] = new boolean[8];
        int materialDifference = 0;
        int support;

        for(int file = 0; file < 8; file++) {
            for(int rank = 0; rank < 8; rank++) {
                switch (board.getSquare(file, rank).occupiedBy()) {
                    case WHITE:
                        whiteSpace[file] = Math.max(whiteSpace[file], rank - 1);
                        //score += (rank - 1); //space points
                        if(whiteFile[file]) score--; //doubled pawns
                        whiteFile[file] = true; //to check how many can block it

                        whiteSupportFile[file]++;
                        if(file > 0)
                            whiteSupportFile[file - 1]++;
                        if(file < 7)
                            whiteSupportFile[file + 1]++;

                        materialDifference++;

                        support = 0; //calc support points
                        if(file > 0) {
                            if(board.getSquare(file - 1, rank + 1).occupiedBy() == Color.BLACK)
                                support--;
                            if(board.getSquare(file - 1, rank - 1).occupiedBy() == Color.WHITE)
                                support++;
                        }
                        if(file < 7) {
                            if(board.getSquare(file + 1, rank + 1).occupiedBy() == Color.BLACK)
                                support--;
                            if(board.getSquare(file + 1, rank - 1).occupiedBy() == Color.WHITE)
                                support++;
                        }
                        if(support < 0) //same as material
                            score = score + MATERIAL * support;

                        break;
                    case BLACK:
                        blackSpace[file] = Math.min(blackSpace[file], rank - 7);
                        //score -= (7 - rank);
                        if(blackFile[file]) score++;
                        blackFile[file] = true;

                        blackSupportFile[file]++;
                        if(file > 0)
                            blackSupportFile[file - 1]++;
                        if(file < 7)
                            blackSupportFile[file + 1]++;

                        materialDifference--;

                        support = 0;
                        if(file > 0) {
                           if(board.getSquare(file - 1, rank - 1).occupiedBy() == Color.WHITE)
                                support--;
                            if(board.getSquare(file - 1, rank + 1).occupiedBy() == Color.BLACK)
                                support++;
                        }
                        if(file < 7) {
                           if(board.getSquare(file + 1, rank - 1).occupiedBy() == Color.WHITE)
                                support--;
                            if(board.getSquare(file + 1, rank + 1).occupiedBy() == Color.BLACK)
                                support++;
                        }
                        if(support < 0)
                            score = score - MATERIAL * support;

                        break;
                    default:
                        break;
                }
            }
        }
        score = score + MATERIAL * materialDifference; //maybe >5


        //calculate minMovesToLose heuristic value
        //closer the passed pawn the more point it gains
        //find column score and aspiring winning pawns with more support
        //calculate extra moves needed though

        int minMovesWhite = 1000;
        int minMovesBlack = 1000;
        int supporter;
        for(int i = 0; i < whiteSupportFile.length; i++) {
            if(whiteSupportFile[i] > blackSupportFile[i]) {
                //can get a passed pawn, minMovesWhite
                //whiteSpace, whiteSupportFile
                if(i == 0)
                    supporter = 7 - whiteSpace[i + 1];
                else if(i == 7)
                    supporter = 7 - whiteSpace[i - 1];
                else
                    supporter = 7 - Math.max(whiteSpace[i - 1], whiteSpace[i + 1]);

                minMovesWhite = 7 - whiteSpace[i] + supporter - 2;
            } else if(whiteSupportFile[i] < blackSupportFile[i]) {
                //black passed pawn, minMovesBlack
                if(i == 0)
                    supporter = blackSpace[i + 1] + 7;
                else if(i == 7)
                    supporter = blackSpace[i - 1] + 7;
                else
                    supporter = 7 + Math.min(blackSpace[i - 1], blackSpace[i + 1]);
                minMovesBlack = blackSpace[i] + 7 + supporter - 2;
            }
        }

        //support array, add supporters
        //space array of each
        //if supporters > corresponding
            //add space + dist_to_support
            //dist_to_support is the others space - 2

        int whiteBestPassed = -1;
        int blackBestPassed = 100;

        for(int i = 0; i < whitePassedPawnList.size(); i++) {
            //find best passed distance
            whiteBestPassed = Math.max(whiteBestPassed, whitePassedPawnList.get(i).getY());
        }

        for(int i = 0; i < blackPassedPawnList.size(); i++) {
            //find best passed distance
            blackBestPassed = Math.min(blackBestPassed, blackPassedPawnList.get(i).getY());
        }


        if(whiteBestPassed == -1 && blackBestPassed == 100)
            return score;

        whiteBestPassed = 7 - whiteBestPassed;
        if(whiteBestPassed < blackBestPassed && whiteBestPassed < minMovesBlack)
            score += 1000;

        if(blackBestPassed < whiteBestPassed && blackBestPassed < minMovesWhite)
            score -= 1000;

        return score;
    }

    private int infinite(Color player) {
        switch(player) {
            case WHITE:
                return 10000; //inf
            case BLACK:
                return -10000;
            default:
                return 0;
        }
    }
    private boolean searchEndCondition() {
        //TODO if another end condition comes in mind
        return false;
    }
}
