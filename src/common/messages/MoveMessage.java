package common.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import common.types.MessageType;

public class MoveMessage implements Message {

    private int x;
    private int y;

    public MoveMessage(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public MoveMessage() {
        this.x = 0;
        this.y = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(x);
        out.writeInt(y);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        x = in.readInt();
        y = in.readInt();
    }

    @Override
    public MessageType getType() {
        return MessageType.MOVE;
    }


}
