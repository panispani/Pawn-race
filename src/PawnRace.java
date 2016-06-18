import java.util.Scanner;

/**
 * Created by Panayiotis on 03/01/2016.
 */
public class PawnRace {
    public static void main(String[] args) {
        boolean isHumanPlayer1;
        boolean isHumanPlayer2;
        char whiteGap;
        char blackGap;

        Scanner input = new Scanner(System.in);

        //Game Parameters
        if(args.length == 4) {
            isHumanPlayer1 = args[0].equals("H");
            isHumanPlayer2 = args[1].equals("H");

            whiteGap = args[2].charAt(0);
            blackGap = args[3].charAt(0);
        } else {
            System.out.println("Please give the 2 player types (C - Computer, H - human) ");
            isHumanPlayer1 = input.next().charAt(0) == 'H';
            isHumanPlayer2 = input.next().charAt(0) == 'H';

            System.out.println("Please give the columns where white and black miss a pawn (A - H)");
            whiteGap = input.next().charAt(0);
            blackGap = input.next().charAt(0);
            //TO ADD validity check
        }

        Board board = new Board(whiteGap, blackGap);
        Game game = new Game(board);
        Player p1 = new Player(board, game, Color.WHITE, isHumanPlayer1);
        Player p2 = new Player(board, game, Color.BLACK, isHumanPlayer2);
        int playerTurn = 1;
        String moveSAN;
        Move move;
        board.display();
        while(!game.isFinished()) {

            if(playerTurn == 1) {
                if(isHumanPlayer1) {
                    
                    do {
                        System.out.println("Play your move! ");
                        moveSAN = input.next().toLowerCase();
                    }while(!NotationUtil.validStringMove(moveSAN.toLowerCase(), Color.WHITE, board));
                    moveSAN = NotationUtil.toStandardNotation(moveSAN, Color.WHITE, board);
                    move = game.parseMove(moveSAN);
                    game.applyMove(move);

                } else {
                    //AI
                    p1.makeMove();
                }
                playerTurn = 2;

            } else {

                if(isHumanPlayer2) {

                    do {
                        System.out.println("Play your move! ");
                        moveSAN = input.next().toLowerCase();
                    } while(!NotationUtil.validStringMove(moveSAN.toLowerCase(), Color.BLACK, board));
                    moveSAN = NotationUtil.toStandardNotation(moveSAN, Color.BLACK, board);

                    move = game.parseMove(moveSAN);
                    game.applyMove(move);

                } else {
                    //AI
                    p2.makeMove();
                }
                playerTurn = 1;

            }
            board.display();
        }
        Color winner = game.getGameResult();
        System.out.println(GameUtil.gameResults(winner));
    }
}
