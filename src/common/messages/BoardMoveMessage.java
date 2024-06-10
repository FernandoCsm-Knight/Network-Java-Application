package common.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import common.types.MessageType;

public class BoardMoveMessage implements Message {

    private int id;
    private char[][] board;

    public BoardMoveMessage() {
        this(-1, new char[3][3]);
    }

    public BoardMoveMessage(int id, char[][] board) {
        this.id = id;
        this.board = board;
    }

    public int getId() {
        return this.id;
    }

    public char[][] getBoard() {
        return this.board;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(this.id);
        out.writeInt(board.length);
        out.writeInt(board[0].length);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                out.writeChar(board[i][j]);
            }
        }
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        this.id = in.readInt();
        int rows = in.readInt();
        int cols = in.readInt();
        this.board = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.board[i][j] = in.readChar();
            }
        }
    }

    @Override
    public MessageType getType() {
        return MessageType.BOARD_MOVE;
    }

    @Override
    public String toString() {
        return "{ Type: " + this.getType() + ", Id: " + this.id + ", Board: " + this.board + " }";
    }
    
}
