/**
 * Created by Panayiotis on 07/01/2016.
 */
public class NotationUtil {

    public static boolean validStringMove(String san, Color color, Board board) {

        //implement more serious move validation
        if(san.length() == 5 || san.length() == 2 || san.length() == 4)
            return true;

        return  false;

    }

    public static String toStandardNotation(String san, Color color, Board board) {
        //pre - move is valid

        //already in standard notation
        if(san.length() == 5)
            return san;

        //normal move
        if(san.length() == 2) {
            int file = Character.toLowerCase(san.charAt(0)) - 'a';
            int rank = san.charAt(1) - '1';
            int direction = GameUtil.getDirectionOfPawn(color);
            if(board.getSquare(file, rank - direction).occupiedBy() == color) {
                return "" + (char)(file + 'a') + (char)(rank - direction + '1') + '-' + san;
            }
            else if(board.getSquare(file, rank - 2 * direction).occupiedBy() == color) {
                return "" + (char)(file + 'a') + (char)(rank - 2 * direction + '1') + '-' + san;
            }
            assert(true) : "Invalid move";
            return null;
        }

        //capture
        if(san.length() == 4) {
            int direction = GameUtil.getDirectionOfPawn(color);
            int rank = san.charAt(3) - '1';
            return "" +san.charAt(0) + (char)(rank - direction + '1') + "x"
                    + san.charAt(2)
                    + san.charAt(3);
        }

        //invalid move
        assert(true):
                "move is not valid";
        return null;

    }

}
