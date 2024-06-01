package common.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import common.types.MessageType;

public class SymbolMessage implements Message {
    
    private char symbol;
    
    public SymbolMessage() {
        this.symbol = '\0';
    }

    public SymbolMessage(char symbol) {
        this.symbol = symbol;
    }
    
    public char getSymbol() {
        return symbol;
    }
    
    @Override
    public MessageType getType() {
        return MessageType.SYMBOL;
    }
    
    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeChar(symbol);
    }
    
    @Override
    public void read(DataInputStream in) throws IOException {
        symbol = in.readChar();
    }

    @Override
    public String toString() {
        return "{ Type: " + this.getType() + ", Symbol: " + symbol + " }";
    }
}
