package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import common.DynamicQueue;
import common.messages.Message;
import common.messages.MessageFactory;

public class PlayerHandler implements Runnable {
    
    private Socket socket;
    private DynamicQueue<PlayerHandler> queue;

    public DataOutputStream out;
    public DataInputStream in;
    boolean online = false;

    public PlayerHandler(Socket socket, DynamicQueue<PlayerHandler> queue) {
        this.socket = socket;
        this.queue = queue;

        try {
            this.out = new DataOutputStream(this.socket.getOutputStream());
            this.in = new DataInputStream(this.socket.getInputStream());
        } catch(IOException e) {
            e.printStackTrace();
            this.close();
        }
    }
    
    @Override
    public void run() {
        int length = 0;

        try {
            while(this.in != null && (length = this.in.readInt()) != 0) {
                byte[] data = new byte[length];
                this.in.readFully(data, 0, data.length);

                Message message = MessageFactory.deserialize(data);
                this.handle(message);
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
    }

    private void handle(Message message) {
        switch(message.getType()) {
            case PLAY:
                this.online = true;
                this.queue.enqueue(this);
                System.out.println(this.queue.size());
                break;
            case MOVE:
                break;
            case CANCEL_CONNECTION:
                this.online = false;
                this.queue.remove(this);
                System.out.println(this.queue.size());
                break;
            case QUIT:
                this.close();
                break;
            
            default:
        }
    }

    public void send(Message message) {
        try {
            byte[] data = MessageFactory.serialize(message);
            this.out.writeInt(data.length);
            this.out.write(data);
            this.out.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            if(this.out != null) this.out.close();
            if(this.in != null) this.in.close();
            if(!this.socket.isClosed()) this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
