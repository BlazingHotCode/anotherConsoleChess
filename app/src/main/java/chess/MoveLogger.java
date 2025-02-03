package chess;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MoveLogger {

    private static MoveLogger instance;
    private char turn;
    private int turnCount;
    private String parentFolder = "games";
    private File moveFile;
    private int halfMoveClock; // For 50-move rule
    private List<String> boardStates; // For repetition rule
    private boolean isDraw = false;
    private String drawReason;

    public static MoveLogger getInstance() {
        if (instance == null) {
            instance = new MoveLogger();
        }
        return instance;
    }

    private MoveLogger() {
        turn = 'W';
        turnCount = 1;
        halfMoveClock = 0;
        boardStates = new ArrayList<>();
        createFile();
    }

    public boolean isDraw() {
        return isDraw;
    }

    public String getDrawReason() {
        return drawReason;
    }

    public void logMove(String move) {
        String annotation = annotationToChessAnottation(move);
        addMoveToFile(annotation);
        turn = turn == 'W' ? 'B' : 'W';
        updateDrawConditions(move);
    }

    public void setTurn(char turn) {
        this.turn = turn;
    }

    public char getTurn() {
        return this.turn;
    }

    public void reset() {
        turn = 'W';
        turnCount = 1;
        halfMoveClock = 0;
        boardStates.clear();
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    public int getTurnCount() {
        return this.turnCount;
    }

    private void createFile() {
        File parentDir = new File(parentFolder);
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat day = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String date = sdf.format(new Date());
        String fileName = parentFolder + "/" + date + ".txt";
        File file = new File(fileName);
        int counter = 1;

        while (file.exists()) {
            fileName = parentFolder + "/" + date + "(" + counter + ").txt";
            file = new File(fileName);
            counter++;
        }
        moveFile = file;
        try (FileWriter writer = new FileWriter(moveFile, true)) {
            writer.write(time.format(new Date()) + System.lineSeparator());
            writer.write(day.format(new Date()) + System.lineSeparator() + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }
    }

    private void addMoveToFile(String move) {
        try (FileWriter writer = new FileWriter(moveFile, true)) {
            if (turn == 'W') {
                writer.write(turnCount + ". " + move);
                turnCount++;
            } else {
                writer.write("    " + move + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }
    }

    private void updateDrawConditions(String move) {
        if (move.contains("x") || move.contains("O-O") || move.contains("O-O-O")) {
            halfMoveClock = 0;
        } else {
            halfMoveClock++;
        }

        String boardState = Board.getInstance().toString();
        boardStates.add(boardState);

        if (halfMoveClock >= 50) {
            drawReason = "50-move rule";
            logDraw(drawReason);
            isDraw = true;
        } else if (Collections.frequency(boardStates, boardState) >= 3) {
            drawReason = "threefold repetition";
            logDraw(drawReason);
            isDraw = true;
        } else if (noLegalMoves()) {
            drawReason = "stalemate";
            logDraw(drawReason);
            isDraw = true;
        } else if (insufficientMaterial()) {
            drawReason = "insufficient material";
            logDraw(drawReason);
            isDraw = true;
        }
    }

    private boolean noLegalMoves() {
        Board board = Board.getInstance();
        boolean[][] moves = board.getAllMovesByColor((turn == 'W'));
        for (boolean[] row : moves) {
            for (boolean move : row) {
                if (move) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean insufficientMaterial() {
        Board board = Board.getInstance();
        int whitePieces = 0;
        int blackPieces = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getBoard()[i][j];
                if (piece.getType() != PieceTypes.NONE) {
                    if ("White".equals(piece.getColor())) {
                        whitePieces++;
                    } else {
                        blackPieces++;
                    }
                }
            }
        }
        // Only kings left
        return whitePieces == 1 && blackPieces == 1;
    }

    private void logDraw(String reason) {
        try (FileWriter writer = new FileWriter(moveFile, true)) {
            writer.write("Game drawn by " + reason + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }
    }

    private String annotationToChessAnottation(String move) {
        Board board = Board.getInstance();
        String annotation = "";

        if (move.equals("O-O")) {
            annotation = "O-O";
        } else if (move.equals("O-O-O")) {
            annotation = "O-O-O";
        } else {
            String secPos;
            int y1 = move.charAt(0) - 'a';
            int x1 = move.charAt(1) - '1';
            int y2;
            int x2;
            if (move.charAt(2) == 'x') {
                y2 = move.charAt(3) - 'a';
                x2 = move.charAt(4) - '1';
                secPos = "x";
                secPos += move.substring(3, 5);
            } else {
                y2 = move.charAt(2) - 'a';
                x2 = move.charAt(3) - '1';
                secPos = move.substring(2, 4);
            }
            if (board.getBoard()[x2][y2].getType() != PieceTypes.PAWN
                    && board.getBoard()[x2][y2].getType() != PieceTypes.NONE) {
                annotation += board.getBoard()[x2][y2].toChar();
            }
            annotation += move.substring(0, 2);
            annotation += secPos;
        }

        return annotation;
    }
}
