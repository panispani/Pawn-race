import java.util.Scanner;

/**
 * Created by Panayiotis on 03/01/2016.
 */
public class PawnRace {
    public static void main(String[] args) {

        boolean isHumanPlayer1 = args[0].equals("H");
        boolean isHumanPlayer2 = args[1].equals("H");

        char whiteGap = args[2].charAt(0);
        char blackGap = args[3].charAt(0);

        Board board = new Board(whiteGap, blackGap);
        Game game = new Game(board);
        Player p1 = new Player(board, game, Color.WHITE, isHumanPlayer1);
        Player p2 = new Player(board, game, Color.BLACK, isHumanPlayer2);
        int playerTurn = 1;
        Scanner input = new Scanner(System.in);
        String moveSAN;
        Move move;

        while(game.isFinished()) {
            board.display();
            if(playerTurn == 1) {

                if(isHumanPlayer1) {
                    System.out.println("Play your move! ");
                    moveSAN = input.next();
                    move = game.parseMove(moveSAN);
                    game.applyMove(move);
                } else {
                    p1.makeMove();
                }
                playerTurn = 2;

            } else {

                if(isHumanPlayer2) {
                    System.out.println("Play your move! ");
                    moveSAN = input.next();
                    move = game.parseMove(moveSAN);
                    game.applyMove(move);
                } else {
                    p2.makeMove();
                }
                playerTurn = 1;

            }
        }

        Color winner = game.getGameResult();
        System.out.println(GameUtil.gameResults(winner));
    }
}
