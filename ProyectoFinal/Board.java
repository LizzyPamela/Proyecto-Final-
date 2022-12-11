import java.util.Scanner;
import java.util.LinkedList;

public class Board {
    private final int BOARD_WIDTH = 8;
    private final int BOARD_HEIGHT = 8;
    private LinkedList<Square> squares = new LinkedList();

    public Board() {
        this.build();
    }

    private void build() {
        this.squares.clear();
        for (int y = 1; y <= BOARD_HEIGHT; y++) {
            for (int x = 1; x <= BOARD_WIDTH; x++) {
                this.squares.add(new Square(x, y, new Blank()));}
        }
    }

    public void play() {
        this.printWelcome();
        while (true) {

            // construye o reconstruye el tablero de una jugada anterior
            this.build();

            // solicita y valida la entrada del movimiento, luego crea un objeto de movimiento
            Move move = null;
            while (move == null) {
                try {
                    move = new Move(this.readMove(), this);
                } catch (IllegalArgumentException e) {
                    System.out.println("ERROR: " + e.getMessage() + " Please try again.");  }
            }

            // fijar la casilla de origen con una pieza de caballo, resolver la jugada, e imprimir nuestros tableros + soluciones
            move.getSource().setPiece(new Knight());
            LinkedList<LinkedList<Square>> solutions = getKnightTravailsSolutions(move);
            this.print(solutions);
        }
    }

    private String readMove() {
        Scanner sc = new Scanner(System.in);
        System.out.println();  System.out.println();
        System.out.println("=======================================================================================");
        System.out.print("MOVIMIENTO: ");
        return sc.nextLine();
        
    }

    public Square getSquare(int x, int y) {
        for (Square square: squares) {
            if (square.matches(x, y)) return square;
        }
        return null;
    }

    // FUNCIONES DE KNIGHT TRAVAILS

    public LinkedList<LinkedList<Square>> getKnightTravailsSolutions(Move move) {

        // implementación de la búsqueda breadth-first
        // devuelve una lista de soluciones del camino más corto, cada solución contiene una lista de cuadrados que recorre
        LinkedList<LinkedList<Square>> solutions = new LinkedList();
        LinkedList<LinkedList<Square>> queue = new LinkedList();
        LinkedList<Square> visited = new LinkedList();
        boolean solutionFound = false;

        // inicializar el primer camino, nuestro primer camino es el cuadrado de origen del movimiento
        // esto es técnicamente un camino, imagina en un caso donde nuestro origen y destino es el mismo cuadrado
        LinkedList<Square> firstPath = new LinkedList();
        firstPath.add(move.getSource());
        queue.add(firstPath);

        while (!queue.isEmpty()) {
            LinkedList<Square> currentPath = queue.removeFirst();
            Square currentSquare = currentPath.getLast();

            if (currentSquare == move.getDestination()) {
                solutions.add(currentPath);
                solutionFound = true;
            }

            if (!solutionFound) {
                for (Square nextLegalSquare: this.getKnightNextLegalMoves(currentSquare)) {
                    if (!visited.contains(nextLegalSquare)) {
                        LinkedList<Square> nextPath = new LinkedList();
                        nextPath.addAll(currentPath);
                        nextPath.add(nextLegalSquare);
                        queue.addLast(nextPath);
                    }
                }
            }

            if (!visited.contains(currentSquare)) visited.add(currentSquare);
        }

        return solutions;
    }

    private LinkedList<Square> getKnightNextLegalMoves(Square source) {

        // búsqueda por fuerza bruta para encontrar las siguientes casillas legales a todas las casillas del tablero
        // devuelve una lista de posibles casillas del siguiente movimiento dada una casilla origen
        LinkedList<Square> nextMoves = new LinkedList();
        for (Square destination: squares) {
            int xMoveDistance = destination.getX() - source.getX();
            int yMoveDistance = destination.getY() - source.getY();

            // dado que un caballo salta sobre otras piezas, sólo necesitamos asegurarnos de que no existe ninguna pieza dentro de nuestra casilla de destino
            // y, si nos movimos 2 y luego 1 espacio o 1 y luego 2 espacios
            if ((destination.getPiece().isBlank()) && (Math.abs(xMoveDistance * yMoveDistance) == 2)) {   
                nextMoves.add(destination); 
            }
        }
        return nextMoves;
    }

    // FUNCIONES DE IMPRESION

    private void printWelcome() {
        System.out.println();
        System.out.println();
        System.out.println("=======================================================================================");
        System.out.println("El Reto de Knight's Travails");
        System.out.println("=======================================================================================");
        System.out.println();
        System.out.println("Bienvenido. :) Acepto dos casillas identificadas por la notación algebraica del ajedrez.");
        System.out.println("El primer cuadrado es la posición inicial y el segundo es la posición final..");
        System.out.println("A continuación, encontraré la secuencia más corta de movimientos válidos para tomar una pieza de Caballo del");
        System.out.println("posición inicial a la solución final.");
        System.out.println();
        System.out.println("Ejemplo de entrada sería: A8 B7");
    }

    private void print(LinkedList<LinkedList<Square>> solutions) {
        if (solutions.isEmpty()) {
            System.out.println(" SOLUCIÓN #1: No existe solución");
        } else {
            for (LinkedList<Square> solution: solutions) {
                System.out.println(this.getBoardLine(solution));
                System.out.println(" SOLUCIÓN  #" + ((int)solutions.indexOf(solution) + 1) + ": " + this.getSolutionLine(solution));
            }
        }
    }

    public String getSolutionLine(LinkedList<Square> solution) {
        String line = "";
        if (solution.getFirst() == solution.getLast()) {
            line += "No es necesario desplazarse";
        } else {
            for (Square square: solution) {
                if (square != solution.getFirst()) line += square.toChessNotation() + " ";
            }
        }
        return line;
    }

    private String getBoardLine(LinkedList<Square> solution) {
        String line = "\n";
        line += this.getBoardTopLine() + "\n";
        line += this.getBoardMiddleLine() + "\n";
        for (int y = 1; y <= this.BOARD_HEIGHT; y++) {
            for (int x = 1; x <= this.BOARD_WIDTH; x++) {
                Square square = this.getSquare(x, y);
                if (!square.getPiece().isBlank()) { line += " | " + square.getPiece().toChessNotation();} 
                    else if (solution.contains(square)) {line += " | " + solution.indexOf(square); } 
                    else {line += " | " + square.getPiece().toChessNotation();}
            }
            line += " | " + y + "\n";
            line += this.getBoardMiddleLine() + "\n";
        }
        return line;
    }

    private String getBoardMiddleLine() {
        String line = " ";
        for (int i = 0; i < this.BOARD_WIDTH; i++) {
            line += "+---";
        }
        line += "";
        return line;
    }

    private String getBoardTopLine() {
        String line = " ";
        char startChar = 'a';
        for (int i = 0; i < this.BOARD_WIDTH; i++) {
            line += "  " + startChar + " ";
            startChar++;
        }
        return line;
    }
}
