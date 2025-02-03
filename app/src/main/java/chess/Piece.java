package chess;

import java.awt.Point;

public class Piece {

    private PieceTypes type = PieceTypes.NONE;
    private boolean isWhite = true;
    private int x;
    private int y;
    private boolean enPassantEligible = false;
    private boolean hasMoved = false;
    private boolean isCalculatingOpponentMoves = false;
    private boolean[][] opponentMovesCache = null;

    public Piece(PieceTypes type, boolean isWhite, int x, int y) {
        this.type = type;
        this.isWhite = isWhite;
        this.x = x;
        this.y = y;
    }

    public PieceTypes getType() {
        return type;
    }

    public String getName() {
        return type.getName();
    }

    public String getColor() {
        return isWhite ? "White" : "Black";
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean isBlack() {
        return !isWhite;
    }

    public boolean isNone() {
        return type == PieceTypes.NONE;
    }

    public boolean isPawn() {
        return type == PieceTypes.PAWN;
    }

    public boolean isRook() {
        return type == PieceTypes.ROOK;
    }

    public boolean isKnight() {
        return type == PieceTypes.KNIGHT;
    }

    public boolean isBishop() {
        return type == PieceTypes.BISHOP;
    }

    public boolean isQueen() {
        return type == PieceTypes.QUEEN;
    }

    public boolean isKing() {
        return type == PieceTypes.KING;
    }

    public boolean isSameColor(Piece other) {
        return isWhite == other.isWhite;
    }

    public boolean isOppositeColor(Piece other) {
        return isWhite != other.isWhite;
    }

    public char toChar() {
        return type.toChar();
    }

    public void setPieceType(PieceTypes type) {
        this.type = type;
    }

    public PieceTypes getPieceType() {
        return this.type;
    }

    public boolean isEnPassantEligible() {
        return enPassantEligible;
    }

    public void setEnPassantEligible(boolean enPassantEligible) {
        this.enPassantEligible = enPassantEligible;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    @Override
    public String toString() {
        return (isWhite ? "W" : "B") + type.toChar();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Piece) {
            Piece other = (Piece) obj;
            return type == other.type && isWhite == other.isWhite;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return type.hashCode() + Boolean.hashCode(isWhite);
    }

    private boolean isValidMove(Piece[][] board, int x, int y) {
        return x >= 0 && x < board.length && y >= 0 && y < board[0].length;
    }

    public boolean[][] getPossibleMoves(Piece[][] board, boolean move, boolean take) {
        switch (this.type) {
            case PAWN:
                return getPawnMoves(board, move, take);
            case ROOK:
                return getRookMoves(board);
            case KNIGHT:
                return getKnightMoves(board);
            case BISHOP:
                return getBishopMoves(board);
            case QUEEN:
                return getQueenMoves(board);
            case KING:
                return getKingMoves(board);
            default:
                return new boolean[board.length][board[0].length];
        }
    }

    private boolean[][] getPawnMoves(Piece[][] board, boolean move, boolean take) {
        boolean[][] moves = new boolean[board.length][board[0].length];
        int direction = this.isWhite ? 1 : -1;
        int startRow = this.isWhite ? 1 : 6;

        // Move forward
        if (move && isValidMove(board, this.x + direction, this.y) && board[this.x + direction][this.y].isNone()) {
            moves[this.x + direction][this.y] = true;
            // Move two squares forward from starting position
            if (this.x == startRow && board[this.x + 2 * direction][this.y].isNone()) {
                moves[x + 2 * direction][y] = true;
                this.enPassantEligible = true;
            }
        }

        // Capture diagonally
        if (take) {
            if (isValidMove(board, x + direction, y - 1) && board[x + direction][y - 1].isOppositeColor(this)) {
                moves[x + direction][y - 1] = true;
            }
            if (isValidMove(board, x + direction, y + 1) && board[x + direction][y + 1].isOppositeColor(this)) {
                moves[x + direction][y + 1] = true;
            }
            // En Passant
            if (isValidMove(board, x + direction, y - 1) && board[x][y - 1].isOppositeColor(this) && board[x][y - 1].isEnPassantEligible()) {
                moves[x + direction][y - 1] = true;
            }
            if (isValidMove(board, x + direction, y + 1) && board[x][y + 1].isOppositeColor(this) && board[x][y + 1].isEnPassantEligible()) {
                moves[x + direction][y + 1] = true;
            }
        }

        return moves;
    }

    private boolean[][] getRookMoves(Piece[][] board) {
        boolean[][] moves = new boolean[board.length][board[0].length];

        // Move vertically
        for (int i = x - 1; i >= 0; i--) {
            if (board[i][y].isNone()) {
                moves[i][y] = true;
            } else if (board[i][y].isOppositeColor(this)) {
                moves[i][y] = true;
                break;
            } else {
                break;
            }
        }
        for (int i = x + 1; i < board.length; i++) {
            if (board[i][y].isNone()) {
                moves[i][y] = true;
            } else if (board[i][y].isOppositeColor(this)) {
                moves[i][y] = true;
                break;
            } else {
                break;
            }
        }

        // Move horizontally
        for (int j = y - 1; j >= 0; j--) {
            if (board[x][j].isNone()) {
                moves[x][j] = true;
            } else if (board[x][j].isOppositeColor(this)) {
                moves[x][j] = true;
                break;
            } else {
                break;
            }
        }
        for (int j = y + 1; j < board[0].length; j++) {
            if (board[x][j].isNone()) {
                moves[x][j] = true;
            } else if (board[x][j].isOppositeColor(this)) {
                moves[x][j] = true;
                break;
            } else {
                break;
            }
        }

        return moves;
    }

    private boolean[][] getKnightMoves(Piece[][] board) {
        boolean[][] moves = new boolean[board.length][board[0].length];
        int[] dx = {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] dy = {-1, 1, -2, 2, -2, 2, -1, 1};

        for (int i = 0; i < 8; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            if (isValidMove(board, newX, newY) && (board[newX][newY].isNone() || board[newX][newY].isOppositeColor(this))) {
                moves[newX][newY] = true;
            }
        }

        return moves;
    }

    private boolean[][] getBishopMoves(Piece[][] board) {
        boolean[][] moves = new boolean[board.length][board[0].length];
        int[] dx = {-1, -1, 1, 1};
        int[] dy = {-1, 1, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            while (isValidMove(board, newX, newY)) {
                if (board[newX][newY].isNone()) {
                    moves[newX][newY] = true;
                } else if (board[newX][newY].isOppositeColor(this)) {
                    moves[newX][newY] = true;
                    break;
                } else {
                    break;
                }
                newX += dx[i];
                newY += dy[i];
            }
        }

        return moves;
    }

    private boolean[][] getQueenMoves(Piece[][] board) {
        boolean[][] moves = new boolean[board.length][board[0].length];
        boolean[][] rookMoves = getRookMoves(board);
        boolean[][] bishopMoves = getBishopMoves(board);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                moves[i][j] = rookMoves[i][j] || bishopMoves[i][j];
            }
        }

        return moves;
    }

    private boolean[][] getKingMoves(Piece[][] board) {
        boolean[][] moves = new boolean[board.length][board[0].length];
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        // Get all opponent moves from cache
        // boolean[][] opponentMoves = getCachedOpponentMoves(board, false, true, this.isWhite);

        for (int i = 0; i < 8; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            if (isValidMove(board, newX, newY)
                    && (board[newX][newY].isNone() || board[newX][newY].isOppositeColor(this))
                    /*&& !opponentMoves[newX][newY]*/) {
                moves[newX][newY] = true;
            }
        }

        // Castling
        if (!this.hasMoved) {
            // Short castling
            if (isValidMove(board, x, y + 1) && isValidMove(board, x, y + 2)
                    && board[x][y + 1].isNone() && board[x][y + 2].isNone()
                    && board[x][y + 3].isRook() && !board[x][y + 3].hasMoved()
                    /*&& !opponentMoves[x][y + 1] && !opponentMoves[x][y + 2]*/) {
                moves[x][y + 2] = true;
            }
            // Long castling
            if (isValidMove(board, x, y - 1) && isValidMove(board, x, y - 2) && isValidMove(board, x, y - 3)
                    && board[x][y - 1].isNone() && board[x][y - 2].isNone() && board[x][y - 3].isNone()
                    && board[x][y - 4].isRook() && !board[x][y - 4].hasMoved()
                    /*&& !opponentMoves[x][y - 1] && !opponentMoves[x][y - 2] && !opponentMoves[x][y - 3]*/) {
                moves[x][y - 2] = true;
            }
        }

        return moves;
    }

    public boolean[][] getAllMovesByColor(Piece[][] board, boolean move, boolean take, boolean isWhite) {
        boolean[][] moves = new boolean[board.length][board[0].length];

        for (Piece[] board1 : board) {
            for (Piece square : board1) {
                if (square.isWhite == isWhite) {
                    boolean[][] pieceMoves = square.getPossibleMoves(board, move, take);
                    for (int k = 0; k < board.length; k++) {
                        for (int l = 0; l < board[k].length; l++) {
                            moves[k][l] = moves[k][l] || pieceMoves[k][l];
                        }
                    }
                }
            }
        }

        return moves;
    }

    private boolean[][] getCachedOpponentMoves(Piece[][] board, boolean move, boolean take, boolean isWhite) {
        if (opponentMovesCache == null) {
            opponentMovesCache = getAllOpponentMoves(board, move, take, isWhite);
        }
        return opponentMovesCache;
    }

    private boolean[][] getOpponentKingMoves(Piece[][] board, boolean move, boolean take, boolean isWhite) {
        for (Piece[] board1 : board) {
            for (Piece piece : board1) {
                if (piece.isKing() && piece.isWhite != isWhite) {
                    return piece.getPossibleMoves(board, move, take);
                }
            }
        }
        return new boolean[board.length][board[0].length];
    }

    public boolean[][] getAllOpponentMoves(Piece[][] board, boolean move, boolean take, boolean isWhite) {
        if (isCalculatingOpponentMoves) {
            return new boolean[board.length][board[0].length];
        }
        boolean[][] moves = new boolean[board.length][board[0].length];
        isCalculatingOpponentMoves = true;

        for (Piece[] board1 : board) {
            for (Piece piece : board1) {
                if (piece.isKing()) {
                    continue;
                }
                if (piece.isWhite != isWhite) {
                    boolean[][] pieceMoves = piece.getPossibleMoves(board, move, take);
                    for (int k = 0; k < board.length; k++) {
                        for (int l = 0; l < board[k].length; l++) {
                            moves[k][l] = moves[k][l] || pieceMoves[k][l];
                        }
                    }
                }
            }
        }
        isCalculatingOpponentMoves = false;
        return moves;
    }

    public static int BoolArrToBitMap(boolean[][] arr) {
        int bitmap = 0;
        for (boolean[] booleans : arr) {
            for (int j = 0; j < arr[0].length; j++) {
                bitmap <<= 1;
                bitmap |= booleans[j] ? 1 : 0;
            }
        }
        return bitmap;
    }

    public boolean movePiece(Piece[][] board, int x, int y, boolean whiteMove) {
        opponentMovesCache = null; // Invalidate cache
        if (this.getPossibleMoves(board, true, false)[x][y]) {
            if (board[this.x][this.y].isWhite != whiteMove) {
                System.out.println("Not your turn");
                return false;
            }
            // Castling move
            if (this.isKing() && Math.abs(this.y - y) == 2) {
                if (y > this.y) {
                    // Short castling
                    board[this.x][this.y + 1] = board[this.x][this.y + 3];
                    board[this.x][this.y + 3] = new Piece(PieceTypes.NONE, board[this.x][this.y + 1].isWhite(), this.x, this.y + 3);
                    board[this.x][this.y + 1].setPosition(this.x, this.y + 1);
                } else {
                    // Long castling
                    board[this.x][this.y - 1] = board[this.x][this.y - 4];
                    board[this.x][this.y - 4] = new Piece(PieceTypes.NONE, board[this.x][this.y - 1].isWhite(), this.x, this.y - 4);
                    board[this.x][this.y - 1].setPosition(this.x, this.y - 1);
                }
            }
            board[x][y] = this;
            board[this.x][this.y] = new Piece(PieceTypes.NONE, true, this.x, this.y);
            this.x = x;
            this.y = y;
            this.hasMoved = true;
            return true;
        } else {
            System.out.println("Invalid move");
            return false;
        }
    }

    public boolean takePiece(Piece[][] board, int x, int y, boolean whiteMove) {
        opponentMovesCache = null; // Invalidate cache
        if (this.getPossibleMoves(board, false, true)[x][y]) {
            if (board[this.x][this.y].isWhite != whiteMove) {
                System.out.println("Not your turn");
                return false;
            }
            if (board[x][y].isNone() && this.isPawn() && Math.abs(this.y - y) == 1) {
                // En Passant capture
                board[x - (this.isWhite ? 1 : -1)][y] = new Piece(PieceTypes.NONE, true, x, y);
            } else if (board[x][y].isNone()) {
                System.out.println("Can't take an empty space");
                return false;
            } else if (board[x][y].isSameColor(this)) {
                System.out.println("Can't take your own piece");
                return false;
            }
            board[x][y] = this;
            board[this.x][this.y] = new Piece(PieceTypes.NONE, true, x, y);
            this.x = x;
            this.y = y;
            return true;
        } else {
            System.out.println("Invalid move");
            return false;
        }
    }

    public boolean isCheck(Piece[][] board) {
        boolean[][] opponentMoves = getCachedOpponentMoves(board, true, true, this.isWhite);
        return opponentMoves[this.x][this.y];
    }

    public boolean isCheckmate(Piece[][] board) {
        boolean[][] allMoves = this.getPossibleMoves(board, true, true);
        for (int i = 0; i < allMoves.length; i++) {
            for (int j = 0; j < allMoves[i].length; j++) {
                if (allMoves[i][j]) {
                    Piece[][] newBoard = new Piece[board.length][board[0].length];
                    for (int k = 0; k < board.length; k++) {
                        System.arraycopy(board[k], 0, newBoard[k], 0, board[k].length);
                    }
                    newBoard[i][j] = new Piece(this.type, this.isWhite, i, j);
                    newBoard[this.x][this.y] = new Piece(PieceTypes.NONE, true, this.x, this.y);
                    if (!newBoard[i][j].isCheck(newBoard)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
