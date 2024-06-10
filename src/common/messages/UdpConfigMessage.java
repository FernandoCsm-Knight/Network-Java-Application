package common.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import common.types.MessageType;

public class UdpConfigMessage implements Message {
    private int port;

    public UdpConfigMessage() {
        this(-1);
    }

    public UdpConfigMessage(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(port);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        this.port = in.readInt();
    }

    @Override
    public MessageType getType() {
        return MessageType.UDP_CONFIG;
    }

    @Override
    public String toString() {
        return "{ Type: " + this.getType() + ", Port: " + this.port + " }";
    }
    
}
