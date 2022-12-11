public class Knight extends Piece {
    public PieceType getPieceType() { 
        return PieceType.KNIGHT; 
    }

    public String toChessNotation() {
        return "N";
    }

}