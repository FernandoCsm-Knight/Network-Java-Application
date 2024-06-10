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
            case GAME_STATUS:
                return new StatusMessage();
            case PLAYER_STATUS:
                return new PlayerStatusMessage();
            case PLAY:
                return new PlayMessage();
            case CANCEL_CONNECTION:
                return new CancelConnectionMessage();
            case MOVE:
                return new MoveMessage();
            case SYMBOL:
                return new SymbolMessage();
            case TURN:
                return new TurnMessage();
            case CONNECTION:
                return new ConnectionMessage();
            case EXIT:
                return new ExitMessage();
            case BOARD_MOVE:
                return new BoardMoveMessage();
            case UDP_CONFIG:
                return new UdpConfigMessage();
            case ROOM_STATUS:
                return new RoomStatusMessage();
        }

        return null;
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
