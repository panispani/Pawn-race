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

    private Color nextPlayer(Color color) {
        return color == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    public void applyMove(Move move) {

        Moves[nextMoveIndex] = move;
        nextMoveIndex++;
        board.applyMove(move);
        currentPlayer = nextPlayer(currentPlayer);

    }

    public void unapplyMove() {

        if (nextMoveIndex > 0) {
            board.unapplyMove(Moves[nextMoveIndex - 1]);
            currentPlayer = nextPlayer(currentPlayer);
            nextMoveIndex--;
        }

    }

    //helper function for isFinished()
    private boolean canMove(int file, int rank, Color player) {

        int direction = 0;
        switch (player) {
            case BLACK:
                direction = +1;
                break;
            case WHITE:
                direction = -1;
                break;
            default:
                assert (true) : "Player should be BLACK/WHITE";
                break;
        }

        //move forward
        if (board.getSquare(file, rank + direction).occupiedBy() == Color.NONE)
            return true;

        //capture
        if (file < 7 && board.getSquare(file + 1, rank + direction).occupiedBy() == nextPlayer(player))
            return true;

        if (file > 0 && board.getSquare(file - 1, rank + direction).occupiedBy() == nextPlayer(player))
            return true;

        //enpassant capture


        return false;

    }

    //helper function for isFinished()
    private boolean currentPlayerHasNoMoves(int whiteMoves, int blackMoves) {

        switch (currentPlayer) {
            case WHITE:
                return whiteMoves == 0;
            case BLACK:
                return blackMoves == 0;
        }

        assert (true) :
                "Player should be Black or White";
        return false;

    }
}
