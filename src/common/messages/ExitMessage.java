package common.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import common.types.MessageType;

public class ExitMessage implements Message {

    public ExitMessage() {
    
    }
   
    @Override
    public void write(DataOutputStream out) throws IOException {

    }

    @Override
    public void read(DataInputStream in) throws IOException {

    }

    @Override
    public MessageType getType() {
        return MessageType.EXIT;
    }

    @Override
    public String toString() {
        return "{ Type: " + this.getType() + " }";
    }
}
