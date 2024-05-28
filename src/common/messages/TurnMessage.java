package common.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import common.types.MessageType;

public class TurnMessage implements Message {

    private boolean turn;

    public TurnMessage() {
        this.turn = false;
    }

    public TurnMessage(boolean turn) {
        this.turn = turn;
    }

    @Override
    public MessageType getType() {
        return MessageType.TURN;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeBoolean(turn);
    }
    
    @Override
    public void read(DataInputStream in) throws IOException {
        this.turn = in.readBoolean();
    }
    
}
