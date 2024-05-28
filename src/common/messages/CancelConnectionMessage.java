package common.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import common.types.MessageType;

public class CancelConnectionMessage implements Message {
        
        public CancelConnectionMessage() {
    
        }
    
        @Override
        public MessageType getType() {
            return MessageType.CANCEL_CONNECTION;
        }
    
        @Override
        public void write(DataOutputStream out) throws IOException {
        
        }
        
        @Override
        public void read(DataInputStream in) throws IOException {
        
        }
}
