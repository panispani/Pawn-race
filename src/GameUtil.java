/**
 * Created by Panayiotis on 03/01/2016.
 */
public class GameUtil {
    //utility class for Game class

    public static Color oppositePlayer(Color color) {
        return color == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    public static boolean blackIsOutOfPawns(Board board) {

        for(int file = 0; file < 8; file++) {
            for(int rank = 0; rank < 8; rank++){
                if(board.getSquare(file, rank).occupiedBy() == Color.BLACK)
                    return false;
            }
        }
        return true;

    }

    public static boolean whiteIsOutOfPawns(Board board) {

        for(int file = 0; file < 8; file++) {
            for(int rank = 0; rank < 8; rank++){
                if(board.getSquare(file, rank).occupiedBy() == Color.WHITE)
                    return false;
            }
        }
        return true;

    }

    public static boolean pawnOnLastRank(Board board, Color playerColor) {

        for(int file = 0; file < 8; file++) {
            if(board.getSquare(file, 0).occupiedBy() == playerColor)
                return true;
            if(board.getSquare(file, 7).occupiedBy() == playerColor)
                return true;
        }
        return false;

    }

    public static boolean stealMate(Board board, Color currentPlayer, Move lastMove) {

        for(int file = 0; file < 8; file++) {
            for(int rank = 0; rank < 8; rank++){
                if(board.getSquare(file, rank).occupiedBy() == currentPlayer) {
                    if(canMove(board, file, rank, lastMove))
                        return false;
                }
            }
        }
        //no possible moves found, checked all pawns
        return true;
    }

    private static boolean canMove(Board board, int file, int rank, Move lastMove) {

        if(forwardMove(board, file, rank))
            return true;
        if(captureMove(board, file, rank))
            return true;
        if(enPassantCaptureMove(board, file, rank, lastMove))
            return true;

        return false;
    }

    private static boolean forwardMove(Board board, int file, int rank) {
        Color pawnColor = board.getSquare(file, rank).occupiedBy();
        int direction = getDirectionOfPawn(pawnColor);
        if(board.getSquare(file, rank + direction).occupiedBy() == Color.NONE)
            return true;
        return false;
    }
    private static boolean captureMove(Board board, int file, int rank) {
        Color pawnColor = board.getSquare(file, rank).occupiedBy();
        int direction = getDirectionOfPawn(pawnColor);

        if(file > 0 && board.getSquare(file - 1, rank + direction).occupiedBy() == oppositePlayer(pawnColor))
            return true;
        
        if(file < 7 && board.getSquare(file + 1, rank + direction).occupiedBy() == oppositePlayer(pawnColor))
            return true;
        
        return false;
    }

    private static boolean enPassantCaptureMove(Board board, int file, int rank, Move lastMove) {
        Color pawnColor = board.getSquare(file, rank).occupiedBy();
        int direction = getDirectionOfPawn(pawnColor);

        if(!lastMove.isEnPassantCapture())
            return false;

        int enPassantPawnX = lastMove.getTo().getX();
        int enPassantPawnY = lastMove.getTo().getY();
        int toMovePawnX = board.getSquare(file, rank).getX();
        int toMovePawnY = board.getSquare(file, rank).getY();

        if(enPassantPawnY == toMovePawnY &&
                (enPassantPawnX == toMovePawnX + 1 || enPassantPawnX == toMovePawnX - 1))
            return true;

        return false;
    }

    private static int getDirectionOfPawn(Color pawn) {
        switch(pawn) {
            case WHITE: return 1;
            case BLACK: return -1;
            default: assert(true): "Pawn should be WHITE/BLACK"; return 0;
        }
    }
}
