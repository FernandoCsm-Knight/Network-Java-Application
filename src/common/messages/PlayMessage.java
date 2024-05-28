package common.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import common.types.MessageType;

public class PlayMessage implements Message {
    
    public PlayMessage() {

    }

    @Override
    public MessageType getType() {
        return MessageType.PLAY;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
    
    }
    
    @Override
    public void read(DataInputStream in) throws IOException {
    
    }
}
