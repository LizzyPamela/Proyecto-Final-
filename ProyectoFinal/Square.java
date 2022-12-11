public class Square {
    private int x, y;
    private Piece piece;

    public Square(int x, int y, Piece piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
    }

    public int getX() { return this.x; } 

    public int getY() { return this.y; }

    public Piece getPiece() { return this.piece; }

    public void setPiece(Piece piece) { this.piece = piece; }

    public String toChessNotation() {
        return (char)(this.x + 64) + "" + (this.y);
    }

    public boolean matches(int x, int y) {
        return (this.x == x && this.y == y);
    }
}