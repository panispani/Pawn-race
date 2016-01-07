/**
 * Created by Panayiotis on 02/01/2016.
 */
public class Game {

    private Move[] Moves = new Move[100];
    private int nextMoveIndex = 0;
    private Board board;
    private Color currentPlayer = Color.WHITE;

    public Game(Board board) {
        this.board = board;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public Move getLastMove() {
        if (nextMoveIndex == 0)
            return null;
        else
            return Moves[nextMoveIndex - 1];
    }

    public void applyMove(Move move) {

        assert(move != null):
                "applied move shouldn't be null";

        Moves[nextMoveIndex] = move;
        nextMoveIndex++;
        board.applyMove(move);
        currentPlayer = GameUtil.oppositePlayer(currentPlayer);
    }

    public void unapplyMove() {

        if (nextMoveIndex > 0) {
            board.unapplyMove(Moves[nextMoveIndex - 1]);
            currentPlayer = GameUtil.oppositePlayer(currentPlayer);
            nextMoveIndex--;
        }

    }

    public boolean isFinished() {

        if(nextMoveIndex < 1)
            return false;

        //last move was a winning move - pawn promotion
        if(GameUtil.pawnOnLastRank(board, Color.WHITE) ||
                GameUtil.pawnOnLastRank(board, Color.BLACK))
            return true;

        //current player has no valid moves - stealmate
        //nextMove index is at least 1

        if(GameUtil.stealMate(board, currentPlayer, getLastMove()))
            return true;

        //all pawns of a color are captured
        if(GameUtil.whiteIsOutOfPawns(board) ||
                GameUtil.blackIsOutOfPawns(board))
            return true;

        return false;
    }

    public Color getGameResult() {

        if(isFinished()){

            assert(nextMoveIndex > 0):
                    "No moves have been played, game shouldn't be over";

            if(GameUtil.pawnOnLastRank(board, Color.WHITE))
                return Color.WHITE;
            if(GameUtil.pawnOnLastRank(board, Color.BLACK))
                return Color.BLACK;
            Move lastMove = Moves[nextMoveIndex - 1];
            if(GameUtil.stealMate(board, currentPlayer, lastMove))
                return Color.NONE;
            if(GameUtil.whiteIsOutOfPawns(board))
                return Color.BLACK;
            if(GameUtil.blackIsOutOfPawns(board))
                return Color.WHITE;
            assert(true):
                    "Game shouldn't be finished";

        }
        return null;
    }

    public Move parseMove(String san) {
        //return move object from standard algebraic notation
        //null if not valid
        return GameUtil.stringToMove(san, getLastMove(), nextMoveIndex, board, currentPlayer);
    }


}
