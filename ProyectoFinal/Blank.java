public class Blank extends Piece {
    public PieceType getPieceType() { 
        return PieceType.BLANK; 
    }

    public String toChessNotation() {
        return " ";
    }
}