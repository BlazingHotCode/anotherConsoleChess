package chess;

public enum PieceTypes {
    PAWN,
    ROOK,
    KNIGHT,
    BISHOP,
    QUEEN,
    KING,
    NONE,
    ;

    public static PieceTypes fromChar(char c) {
        switch (c) {
            case 'P':
                return PAWN;
            case 'R':
                return ROOK;
            case 'N':
                return KNIGHT;
            case 'B':
                return BISHOP;
            case 'Q':
                return QUEEN;
            case 'K':
                return KING;
            default:
                return NONE;
        }
    }

    public char toChar() {
        switch (this) {
            case PAWN:
                return 'P';
            case ROOK:
                return 'R';
            case KNIGHT:
                return 'N';
            case BISHOP:
                return 'B';
            case QUEEN:
                return 'Q';
            case KING:
                return 'K';
            default:
                return '.';
        }
    }

    public String getName() {
        switch (this) {
            case PAWN:
                return "Pawn";
            case ROOK:
                return "Rook";
            case KNIGHT:
                return "Knight";
            case BISHOP:
                return "Bishop";
            case QUEEN:
                return "Queen";
            case KING:
                return "King";
            default:
                return "None";
        }
    }
}
