/**
 * Created by Panayiotis on 02/01/2016.
 */
public class Board {

    private Square[][] board = new Square[8][8];

    public Board(char whiteGap, char blackGap) {
        //initial board setup
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {

                board[i][j] = new Square(i, j);
                if(j == 1 && whiteGap != i + 'A') {             //white row
                    board[i][j].setOccupier(Color.WHITE);
                } else if (j == 6 && blackGap != i + 'A') {     //black row
                    board[i][j].setOccupier(Color.BLACK);
                } else {
                    board[i][j].setOccupier(Color.NONE);
                }

            }
        }
    }

    public Square getSquare(int x, int y) {
        return board[x][y];
    }

    public void applyMove(Move move) {

        int fromX = move.getFrom().getX();
        int fromY = move.getFrom().getY();
        System.out.println(board[fromX][fromY].occupiedBy());
        int toX   = move.getTo().getX();
        int toY   = move.getTo().getY();
        Color pawnColor = board[fromX][fromY].occupiedBy();
        board[fromX][fromY].setOccupier(Color.NONE);
        board[toX][toY].setOccupier(pawnColor);

    }

    public void unapplyMove(Move move) {

        int fromX = move.getFrom().getX();
        int fromY = move.getFrom().getY();
        int toX = move.getTo().getX();
        int toY = move.getTo().getY();
        Color pawnColor = board[fromX][fromY].occupiedBy();

        board[fromX][fromY].setOccupier(pawnColor);

        if (move.isEnPassantCapture()) {
            //enPassant 'to' pawn has the same y coordinate as 'from' pawn
            Color opponentColor = pawnColor == Color.BLACK ? Color.WHITE : Color.BLACK;
            board[toX][fromY].setOccupier(opponentColor);
        } else if (move.isCapture()) {
            Color opponentColor = pawnColor == Color.BLACK ? Color.WHITE : Color.BLACK;
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
        System.out.println("    A B C D E F G H\n");

        for(int rank = 8; rank > 0; rank--) {
            System.out.print(rank + "  ");
            for(int file = 1; file <= 8; file++) {
                Color occupier = board[file - 1][rank - 1].occupiedBy();
                char c = colorToSymbol(occupier);
                System.out.print(c);
            }
            System.out.println("  " + rank);
        }

        System.out.println("\n    A B C D E F G H\n");
    }

}
