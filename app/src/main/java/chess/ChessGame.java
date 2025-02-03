package chess;

import java.util.Scanner;

public class ChessGame {

    private static ChessGame instance;

    public static ChessGame getInstance() {
        if (instance == null) {
            instance = new ChessGame();
        }
        return instance;
    }

    private Board board;
    private MoveLogger moveLogger;
    private String moveRegex = "[a-h][1-8][a-h][1-8]";
    private String takeRegex = "[a-h][1-8]x[a-h][1-8]";
    private String castlingRegex = "O-O|O-O-O";

    private ChessGame() {
        this.board = Board.getInstance();
        this.moveLogger = MoveLogger.getInstance();
    }

    private void print() {
        this.board.printBoard();
    }

    public void startGame() {
        this.board.resetBoard();
        // this.board.setBoardZero();
        // this.board.setPiece(PieceTypes.KING, true, 0, 4);
        // this.board.setPiece(PieceTypes.KING, false, 7, 4);
        // this.board.setPiece(PieceTypes.ROOK, true, 0, 0);
        // this.board.setPiece(PieceTypes.ROOK, true, 0, 7);
        // this.board.setPiece(PieceTypes.ROOK, false, 7, 0);
        // this.board.setPiece(PieceTypes.ROOK, false, 7, 7);
        this.board.setPiecesLocations();
        this.moveLogger.reset();
        this.print();
    }

    public boolean movePiece(String move) {
        if (move.matches(castlingRegex)) {
            return handleCastling(move);
        }

        int y1 = move.charAt(0) - 'a';
        int x1 = move.charAt(1) - '1';
        int y2;
        int x2;
        if (move.charAt(2) == 'x') {
            y2 = move.charAt(3) - 'a';
            x2 = move.charAt(4) - '1';
            return this.board.takePiece(x1, y1, x2, y2, this.moveLogger.getTurn() == 'W');
        } else {
            y2 = move.charAt(2) - 'a';
            x2 = move.charAt(3) - '1';
            return this.board.movePiece(x1, y1, x2, y2, this.moveLogger.getTurn() == 'W');
        }
    }

    private boolean handleCastling(String move) {
        int row = this.moveLogger.getTurn() == 'W' ? 0 : 7;
        if (move.equals("O-O")) {
            return this.board.movePiece(row, 4, row, 6, this.moveLogger.getTurn() == 'W');
        } else if (move.equals("O-O-O")) {
            return this.board.movePiece(row, 4, row, 2, this.moveLogger.getTurn() == 'W');
        }
        return false;
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        while (!this.board.isBlackWon() && !this.board.isWhiteWon()) {
            if (MoveLogger.getInstance().isDraw()) {
                System.out.println("Game drawn by " + MoveLogger.getInstance().getDrawReason());
                break;
            }
            System.out.println((this.moveLogger.getTurn() == 'W' ? "White" : "Black") + ", Enter your move: ");
            String move = scanner.nextLine();
            if (move.equals("exit")) {
                break;
            }
            if (!move.matches(this.moveRegex) && !move.matches(this.takeRegex) && !move.matches(this.castlingRegex)) {
                System.out.println("Invalid move. Try again.");
                continue;
            }
            if (movePiece(move)) {
                this.moveLogger.logMove(move);
                this.board.setPiecesLocations();
                this.print();
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }
    }
}
