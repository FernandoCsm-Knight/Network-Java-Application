package common.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import common.types.GameStatus;
import common.types.MessageType;

public class RoomStatusMessage implements Message {
    
    private int id;
    private String status;
    private GameStatus gameStatus;

    public RoomStatusMessage() {
        this("");
    }

    public RoomStatusMessage(String status) {
        this(status, GameStatus.NONE);
    }

    public RoomStatusMessage(String status, GameStatus gameStatus) {
        this(status, gameStatus, -1);
    }

    public RoomStatusMessage(String status, GameStatus gameStatus, int id) {
        this.id = id;
        this.status = status;
        this.gameStatus = gameStatus;
    }

    public int getId() {
        return this.id;
    }

    public String getStatus() {
        return status;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    @Override
    public MessageType getType() {
        return MessageType.ROOM_STATUS;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(this.id);
        out.writeInt(this.gameStatus.ordinal());
        out.writeUTF(status);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        this.id = in.readInt();
        this.gameStatus = GameStatus.values()[in.readInt()];
        this.status = in.readUTF();
    }

    @Override
    public String toString() {
        return "{ Type: " + this.getType() + ", Id: " + this.id + ", Status: " + this.status + ", GameStatus: " + this.gameStatus + " }";
    }

}
