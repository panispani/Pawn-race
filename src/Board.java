/**
 * Created by Panayiotis on 02/01/2016.
 */
public class Board {

    private Square[][] board = new Square[8][8];

    public Board(char whiteGap, char blackGap) {

        //pre - whiteGap, blackGap are UpperCase from 'A' to 'H'
        whiteGap = Character.toUpperCase(whiteGap);
        blackGap = Character.toUpperCase(blackGap);
        assert(whiteGap >= 'A' && blackGap >= 'A' &&
                whiteGap <= 'H' && blackGap <= 'H'):
                "invalid pawn gaps";

        //initial board setup
        for(int file = 0; file < 8; file++) {
            for(int rank = 0; rank < 8; rank++) {

                board[file][rank] = new Square(file, rank);
                if(rank == 1 && whiteGap != file + 'A') {             //white row
                    board[file][rank].setOccupier(Color.WHITE);
                } else if (rank == 6 && blackGap != file + 'A') {     //black row
                    board[file][rank].setOccupier(Color.BLACK);
                } else {
                    board[file][rank].setOccupier(Color.NONE);
                }

            }
        }
    }

    public Square getSquare(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7) {
            System.out.println("possible error " + x + " " + y);
            return new Square(-1, -1);
        }

        return board[x][y];
    }

    public void applyMove(Move move) {

        int fromX = move.getFrom().getX();
        int fromY = move.getFrom().getY();
        int toX   = move.getTo().getX();
        int toY   = move.getTo().getY();
        Color pawnColor = board[fromX][fromY].occupiedBy();
        board[fromX][fromY].setOccupier(Color.NONE);
        board[toX][toY].setOccupier(pawnColor);
        int direction = GameUtil.getDirectionOfPawn(pawnColor);
        if(move.isEnPassantCapture())
            board[toX][toY - direction].setOccupier(Color.NONE);
    }

    public void unapplyMove(Move move) {

        int fromX = move.getFrom().getX();
        int fromY = move.getFrom().getY();
        int toX = move.getTo().getX();
        int toY = move.getTo().getY();
        Color pawnColor = board[toX][toY].occupiedBy();

        board[fromX][fromY].setOccupier(pawnColor);

        if (move.isEnPassantCapture()) {
            //enPassant 'to' pawn has the same y coordinate as 'from' pawn
            board[toX][toY].setOccupier(Color.NONE);
            Color opponentColor = GameUtil.oppositePlayer(pawnColor);
            board[toX][fromY].setOccupier(opponentColor);
        } else if (move.isCapture()) {
            Color opponentColor = GameUtil.oppositePlayer(pawnColor);
            board[toX][toY].setOccupier(opponentColor);
        } else {
            board[toX][toY].setOccupier(Color.NONE);
        }
    }

    private char colorToSymbol(Color color) {
        switch (color) {
            case WHITE:
                return (char) 9823;
            case BLACK:
                return (char) 9817;
            case NONE:
                return '.';
            default:
                return '?';
        }
    }

    public void display() {

        //File names
        System.out.println("   A B C D E F G H\n");

        for(int rank = 8; rank > 0; rank--) {
            System.out.print(rank + "  ");
            for(int file = 1; file <= 8; file++) {
                Color occupier = board[file - 1][rank - 1].occupiedBy();
                char c = colorToSymbol(occupier);
                System.out.print(c + " ");
            }
            System.out.println(" " + rank);
        }

        System.out.println("\n   A B C D E F G H\n");
    }

}
