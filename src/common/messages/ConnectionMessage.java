package common.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import common.types.ConnectionState;
import common.types.MessageType;

public class ConnectionMessage implements Message {
    
    private ConnectionState state;

    public ConnectionMessage() {
        this(ConnectionState.DISCONNECTED);
    }

    public ConnectionMessage(ConnectionState state) {
        this.state = state;
    }

    public ConnectionState getState() {
        return state;
    }

    @Override
    public MessageType getType() {
        return MessageType.CONNECTION;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(state.ordinal());
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        state = ConnectionState.values()[in.readInt()];
    }

    @Override
    public String toString() {
        return "{ Type: " + this.getType() +
               " Connection state: " + this.state + " }";
    }
}
