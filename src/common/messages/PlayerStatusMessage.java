package common.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import common.types.MessageType;
import common.types.PlayerStatus;

public class PlayerStatusMessage implements Message {

    private String status;
    private PlayerStatus playerStatus;

    public PlayerStatusMessage() {
        this("", PlayerStatus.NONE);
    }

    public PlayerStatusMessage(String status, PlayerStatus playerStatus) {
        this.status = status;
        this.playerStatus = playerStatus;
    }

    public String getStatus() {
        return this.status;
    }

    public PlayerStatus getPlayerStatus() {
        return this.playerStatus;
    }

    @Override
    public MessageType getType() {
        return MessageType.PLAYER_STATUS;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(playerStatus.ordinal());
        out.writeUTF(status);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        this.playerStatus = PlayerStatus.values()[in.readInt()];
        this.status = in.readUTF();
    }

    @Override
    public String toString() {
        return "{ Type: " + this.getType() + ", Status: " + this.status + ", PlayerStatus: " + this.playerStatus + " }";
    }
}
