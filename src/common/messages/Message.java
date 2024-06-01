package common.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import common.types.MessageType;

public interface Message {
    public void write(DataOutputStream out) throws IOException;
    public void read(DataInputStream in) throws IOException;
    public MessageType getType();
}
