package client;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import app.services.SettingsService;
import common.integration.ClientListener;
import common.messages.ExitMessage;
import common.messages.Message;
import common.messages.MessageFactory;

public class Client implements Runnable, Closeable {
    
    private Socket socket;
    private DatagramSocket udpSocket;
    private DataOutputStream out;
    private DataInputStream in;
    private boolean connected;
    private boolean udpConnected;
    
    private final List<ClientListener> listeners;
    private final AtomicBoolean running;

    public Client() {
        this(false);
    }

    public Client(boolean tryConnection) {
        if(tryConnection) this.tryToConnect();
        else this.connected = false;

        try {
            this.udpSocket = new DatagramSocket(0);
            this.udpConnected = true;
        } catch(Exception e) {
            this.udpConnected = false;
            e.printStackTrace();
        }

        this.listeners = new ArrayList<>();
        this.running = new AtomicBoolean(true);
    }

    @Override
    public void run() {
        int length = 0;
        this.running.set(true);

        try {
            while(this.running.get() && this.in != null && (length = this.in.readInt()) != 0) {
                byte[] data = new byte[length];
                this.in.readFully(data, 0, data.length);

                Message message = MessageFactory.deserialize(data);
                this.notifyListeners(message);
            }
        } catch (EOFException e) {
            System.out.println("Connection closed.");
        } catch (SocketException e) {
            System.out.println("Connection closed.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
    }

    public DatagramSocket getUdpSocket() {
        return this.udpSocket;
    }

    public void send(Message message) {
        try {
            byte[] data = MessageFactory.serialize(message);
            this.out.writeInt(data.length);
            this.out.write(data);
            this.out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            this.close();
        }
    }

    public void addListener(ClientListener listener) {
        this.listeners.add(listener);
    }

    public void notifyListeners(Message message) {
        System.out.println("Received message: " + message.getType());
        
        for(ClientListener listener : listeners) {
            listener.onMessageReceived(message);
        }
    }

    public boolean isConnected() {
        return this.connected;
    }

    public boolean isUdpConnected() {
        return this.udpConnected;
    }

    public boolean tryToConnect() {
        if(this.connected) return true;

        try {
            this.socket = new Socket(SettingsService.getServerAddress(), SettingsService.getServerPort());
            this.out = new DataOutputStream(this.socket.getOutputStream());
            this.in = new DataInputStream(this.socket.getInputStream());
            this.connected = true;
        } catch (Exception e) {
            this.connected = false;
        }

        return this.connected;
    }

    public void close() {
        this.running.set(false);
        this.connected = this.connected && this.socket != null && !this.socket.isClosed();

        if(this.connected) {
            System.out.println("Trying to close connection...");
            this.connected = false;

            try {
                this.send(new ExitMessage());
            } catch (Exception e) {
                System.out.println("Connection already closed.");
            }
        }

        try {
            if(this.out != null) this.out.close();
            if(this.in != null) this.in.close();
            if(this.socket != null && !this.socket.isClosed()) this.socket.close();
            if(this.udpSocket != null && !this.udpSocket.isClosed()) this.udpSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
