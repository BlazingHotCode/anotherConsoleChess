package chess;

import java.awt.Point;

public final class Board {
    private static Board instance;

    private Piece[][] board;

    private Point whiteKing;
    private Point blackKing;
    private boolean blackWon = false;
    private boolean whiteWon = false;

    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }

    private Board() {
        board = new Piece[8][8];
        setBoardZero();
    }

    public void setBoardZero() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Piece(PieceTypes.NONE, true, i, j);
            }
        }
    }

    public void resetBoard() {
        board[0][0] = new Piece(PieceTypes.ROOK, true, 0, 0);
        board[0][1] = new Piece(PieceTypes.KNIGHT, true, 0, 1);
        board[0][2] = new Piece(PieceTypes.BISHOP, true, 0, 2);
        board[0][3] = new Piece(PieceTypes.QUEEN, true, 0, 3);
        board[0][4] = new Piece(PieceTypes.KING, true, 0, 4);
        whiteKing = new Point(0, 4);
        board[0][5] = new Piece(PieceTypes.BISHOP, true, 0, 5);
        board[0][6] = new Piece(PieceTypes.KNIGHT, true, 0, 6);
        board[0][7] = new Piece(PieceTypes.ROOK, true, 0, 7);
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Piece(PieceTypes.PAWN, true, 1, i);
        }
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Piece(PieceTypes.NONE, false, i, j);
            }
        }
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Piece(PieceTypes.PAWN, false, 6, i);
        }
        board[7][0] = new Piece(PieceTypes.ROOK, false, 7, 0);
        board[7][1] = new Piece(PieceTypes.KNIGHT, false, 7, 1);
        board[7][2] = new Piece(PieceTypes.BISHOP, false, 7, 2);
        board[7][3] = new Piece(PieceTypes.QUEEN, false, 7, 3);
        board[7][4] = new Piece(PieceTypes.KING, false, 7, 4);
        blackKing = new Point(7, 4);
        board[7][5] = new Piece(PieceTypes.BISHOP, false, 7, 5);
        board[7][6] = new Piece(PieceTypes.KNIGHT, false, 7, 6);
        board[7][7] = new Piece(PieceTypes.ROOK, false, 7, 7);
    }

    public void printBoard() {
        System.out.println();
        for (int i = 8 - 1; i >= 0; i--) {
            System.out.print("|");
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getType() == PieceTypes.NONE) {
                    System.out.print("    |");
                } else {
                    System.out.print(" " + board[i][j] + " |");
                }
            }
            System.out.println();
        }
    }

    public Piece[][] getBoard() {
        return board;
    }

    public Piece getPiece(int x, int y) {
        return board[x][y];
    }

    public void setPiece(PieceTypes pieceType, boolean isWhite, int x, int y) {
        board[x][y] = new Piece(pieceType, isWhite, x, y);
    }

    public boolean isBlackWon() {
        return blackWon;
    }

    public boolean isWhiteWon() {
        return whiteWon;
    }

    public boolean movePiece(int x1, int y1, int x2, int y2, boolean whiteMove) {
        boolean move = board[x1][y1].movePiece(board, x2, y2, whiteMove);
        if (move) {
            if (board[x1][y1].getType() == PieceTypes.KING) {
                if (whiteMove) {
                    whiteKing.setLocation(x2, y2);
                } else {
                    blackKing.setLocation(x2, y2);
                }
            }
        }
        return move;
    }

    public boolean takePiece(int x1, int y1, int x2, int y2, boolean whiteMove) {
        boolean move = board[x1][y1].takePiece(board, x2, y2, whiteMove);
        if (move) {
            if (board[x1][y1].getType() == PieceTypes.KING) {
                if (whiteMove) {
                    whiteKing.setLocation(x2, y2);
                } else {
                    blackKing.setLocation(x2, y2);
                }
            } else if (board[x2][y2].getType() == PieceTypes.KING) {
                if (whiteMove) {
                    blackWon = true;
                } else {
                    whiteWon = true;
                }
            }
        }
        return move;
    }

    public void setPiecesLocations() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j].setPosition(i, j);
                if (board[i][j].getType() == PieceTypes.KING) {
                    if (board[i][j].isWhite()) {
                        whiteKing.setLocation(i, j);
                    } else {
                        blackKing.setLocation(i, j);
                    }
                }
            }
        }
    }

    public boolean[][] getAllMovesByColor(boolean whiteMove) {
        boolean[][] allMoves = new boolean[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boolean[][] possibleMoves = board[i][j].getPossibleMoves(board, false, true);
                for (int k = 0; k < 8; k++) {
                    for (int l = 0; l < 8; l++) {
                        allMoves[k][l] = allMoves[k][l] || possibleMoves[k][l];
                    }
                }
            }
        }
        return allMoves;
    }

    public boolean isCheckmate() {
        Point king = MoveLogger.getInstance().getTurn() == 'W' ? whiteKing : blackKing;

        return board[king.x][king.y].isCheckmate(board);
    }
}
