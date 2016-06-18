/**
 * Created by Panayiotis on 02/01/2016.
 */
public class Square {

    private int x;
    private int y;
    private Color color;

    public Square(int x, int y) {

        assert(x >= 0 && x < 8
                && y >= 0 && y < 8):
                "Invalid square: " + x + " " + y;

        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Color occupiedBy() {
        return this.color;
    }

    public void setOccupier(Color color) {
        this.color = color;
    }

    public String getSquareNotation()
    {
        return "" +(char)(x + 'a') + (char)(y + '1');
    }

}
