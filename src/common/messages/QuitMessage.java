package common.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import common.types.MessageType;

public class QuitMessage implements Message {

    public QuitMessage() {

    }

    @Override
    public MessageType getType() {
        return MessageType.QUIT;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
    
    }
    
    @Override
    public void read(DataInputStream in) throws IOException {
    
    }
}
