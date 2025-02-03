package chess;

public class App {
    public static void main(String[] args) {
        System.out.println("Starting Chess Game");
        ChessGame game = ChessGame.getInstance();
        game.startGame();
        game.play();
        
        // Test for Piece class
        // Board board = Board.getInstance();
        // Piece testPiece = new Piece(PieceTypes.KNIGHT, true, 1, 1);
        // System.out.println("Piece Name: " + testPiece.getName()); // Expected: Knight
        // System.out.println("Piece Color: " + testPiece.getColor()); // Expected: White
        // testPiece.setPosition(2, 1);
        // System.out.println("Piece Position: " + testPiece.getPosition()); // Expected: (2, 1)
        // System.out.println("Possible Moves: ");
        // for (boolean[] move : testPiece.getPossibleMoves(board.getBoard(), true, true)) {
        //     System.out.println(Arrays.toString(move));
        // }

    }
}
