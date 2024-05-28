package common.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import common.types.MessageType;

public class SymbolMessage implements Message {
    
    private String symbol;
    
    public SymbolMessage() {
        this.symbol = "";
    }

    public SymbolMessage(String symbol) {
        this.symbol = symbol;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    @Override
    public MessageType getType() {
        return MessageType.SYMBOL;
    }
    
    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(symbol);
    }
    
    @Override
    public void read(DataInputStream in) throws IOException {
        symbol = in.readUTF();
    }

}
