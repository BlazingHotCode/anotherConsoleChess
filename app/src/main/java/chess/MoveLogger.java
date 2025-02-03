package chess;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MoveLogger {

    private static MoveLogger instance;
    private char turn;
    private int turnCount;
    private File moveFile;

    public static MoveLogger getInstance() {
        if (instance == null) {
            instance = new MoveLogger();
        }
        return instance;
    }

    private MoveLogger() {
        turn = 'W';
        turnCount = 1;
        createFile();
    }

    public void logMove(String move) {
        String annotation = annotationToChessAnottation(move);
        addMoveToFile(annotation);
        turn = turn == 'W' ? 'B' : 'W';
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
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    public int getTurnCount() {
        return this.turnCount;
    }

    private void createFile() {
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat day = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String date = sdf.format(new Date());
        String fileName = "games/" + date + ".txt";
        File file = new File(fileName);
        int counter = 1;

        while (file.exists()) {
            fileName = "games/" + date + "(" + counter + ").txt";
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
