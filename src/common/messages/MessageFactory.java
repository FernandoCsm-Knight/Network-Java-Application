package common.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import common.types.MessageType;

public class MessageFactory {
    public static Message createMessage(MessageType type) {
        
        switch (type) {
            case STATUS:
                return new StatusMessage();
            case PLAY:
                return new PlayMessage();
            case QUIT:
                return new QuitMessage();
            case CANCEL_CONNECTION:
                return new CancelConnectionMessage();
            case MOVE:
                return new MoveMessage();
            case SYMBOL:
                return new SymbolMessage();
            default:
                return null;
        }
    }

    public static byte[] serialize(Message message) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(message.getType().ordinal());
        message.write(dos);
        
        return baos.toByteArray();
    }

    public static Message deserialize(byte[] data) throws IOException {
        ByteArrayInputStream baos = new ByteArrayInputStream(data);
        DataInputStream dos = new DataInputStream(baos);

        MessageType type = MessageType.values()[dos.readInt()];
        Message message = createMessage(type);
        message.read(dos);

        return message;
    }
}
