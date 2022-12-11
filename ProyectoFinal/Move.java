public class Move {
    private Square source, destination;

    public Move(String moveInput, Board board) {
        if (moveInput == null || moveInput.length() != 5 || moveInput.charAt(2) != ' ') {
            throw new IllegalArgumentException("Invalid input.");
        }

        int fromX = (int)moveInput.toUpperCase().charAt(0) - '@';
        int fromY = (int)moveInput.toUpperCase().charAt(1) - '0';
        int toX = (int)moveInput.toUpperCase().charAt(3) - '@';
        int toY = (int)moveInput.toUpperCase().charAt(4) - '0';

        //comprobar si las casillas existen realmente
        // p.e. dada la entrada "a9 a1", el cuadrado "a9" podría no existir en el tablero 8x8
        if ((board.getSquare(fromX, fromY) == null) || (board.getSquare(toX, toY)) == null) {
            throw new IllegalArgumentException("Invalid input.");
        }

        this.source = board.getSquare(fromX, fromY);
        this.destination = board.getSquare(toX, toY);
    }

    public Square getSource() { return this.source; }

    public Square getDestination() { return this.destination; }

    public String toChessNotation() {
        String line = this.getSource().toString() + " " 
            + this.getDestination().toString();
        return line;
    }
}