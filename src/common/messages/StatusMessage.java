package common.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import common.types.MessageType;

public class StatusMessage implements Message {
    
    private String status;

    public StatusMessage() {
        this.status = "";
    }

    public StatusMessage(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public MessageType getType() {
        return MessageType.STATUS;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(status);
    }
    
    @Override
    public void read(DataInputStream in) throws IOException {
        status = in.readUTF();
    }

}
