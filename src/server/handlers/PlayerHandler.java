package server.handlers;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import common.messages.Message;
import common.messages.MessageFactory;
import common.messages.MoveMessage;
import common.messages.SymbolMessage;
import common.messages.UdpConfigMessage;
import server.managers.PlayerManager;
import server.services.ServerService;

public class PlayerHandler implements Runnable, Closeable, ServerService {
    
    private MoveMessage move;
    private boolean online;
    private char symbol;
    private int updPort;
    
    private final Socket socket;
    private final Semaphore semaphore;
    private final PlayerManager manager;
    private final AtomicBoolean running;
    
    public DataOutputStream out;
    public DataInputStream in;
    public GameHandler game;

    public PlayerHandler(Socket socket, PlayerManager manager) {
        this.updPort = -1;
        this.online = false;
        this.socket = socket;
        this.manager = manager;
        this.semaphore = new Semaphore(0);
        this.running = new AtomicBoolean(true);

        try {
            this.out = new DataOutputStream(this.socket.getOutputStream());
            this.in = new DataInputStream(this.socket.getInputStream());
        } catch(IOException e) {
            logger.error("Error creating player handler");
            e.printStackTrace();
            this.close();
        }
    }
    
    public Socket getSocket() {
        return this.socket;
    }

    public int getUdpPort() {
        return this.updPort;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
        this.send(new SymbolMessage(symbol));
    }

    public char getSymbol() {
        return this.symbol;
    }

    @Override
    public void run() {
        int length = 0;

        try {
            while(this.running.get() && this.in != null && (length = this.in.readInt()) != 0) {
                byte[] data = new byte[length];
                this.in.readFully(data, 0, data.length);

                Message message = MessageFactory.deserialize(data);
                this.handle(message);
            }
        } catch(IOException e) {
            logger.error("Error reading message");
            e.printStackTrace();
        } finally {
            this.close();
        }
    }

    private void handle(Message message) {
        logger.info("Message received: " + message);
        switch(message.getType()) {
            case PLAY:
                this.manager.connect(this);
                this.online = true;
                break;

            case MOVE:
                move = (MoveMessage) message;
                this.semaphore.release();
                break;

            case CANCEL_CONNECTION:
                this.online = false;
                this.manager.cancelConnection(this);
                break;

            case UDP_CONFIG:
                UdpConfigMessage udpConfig = (UdpConfigMessage) message;
                this.updPort = udpConfig.getPort();
                break;
            
            case EXIT:
                this.online = false;
                this.manager.cancelConnection(this);
                this.close();
                break;
            
            default:
        }
    }

    public MoveMessage receive() throws InterruptedException {
        this.semaphore.acquire();

        MoveMessage mv = this.move;
        this.move = null;

        return mv;
    }

    public void send(Message message) {
        logger.info("Message sent: " + message);

        try {
            byte[] data = MessageFactory.serialize(message);
            this.out.writeInt(data.length);
            this.out.write(data);
            this.out.flush();
        } catch(IOException e) {
            logger.error("Error sending message");
            e.printStackTrace();
        }
    }

    public boolean isClosed() {
        return this.socket.isClosed();
    }

    public boolean isOnline() {
        return this.online;
    }

    @Override
    public void close() {
        this.running.set(false);
        logger.info("Closing player handler");

        try {
            if(this.out != null) this.out.close();
            if(this.in != null) this.in.close();
            if(!this.socket.isClosed()) this.socket.close();
        } catch (IOException e) {
            logger.error("Error closing player handler");
            e.printStackTrace();
        }
    }
}
