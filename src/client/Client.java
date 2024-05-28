package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.integration.ClientListener;
import common.messages.Message;
import common.messages.MessageFactory;

public class Client implements Runnable {
    
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private BufferedReader console;
    private Map<Class<? extends Message>, List<ClientListener<? extends Message>>> listenersMap;

    public Client(String host, int port) {
        try {
            this.socket = new Socket(host, port);
            this.out = new DataOutputStream(this.socket.getOutputStream());
            this.in = new DataInputStream(this.socket.getInputStream());
            this.console = new BufferedReader(new InputStreamReader(System.in));
            this.listenersMap = new HashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
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
                this.notifyListeners(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
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

    public <T extends Message> void addListener(Class<T> messageType, ClientListener<T> listener) {
        listenersMap.computeIfAbsent(messageType, k -> new ArrayList<>()).add(listener);
    }

    public <T extends Message> void removeListener(Class<T> messageType, ClientListener<T> listener) {
        List<ClientListener<? extends Message>> listeners = listenersMap.get(messageType);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    @SuppressWarnings("unchecked")
    private void notifyListeners(Message message) {
        List<ClientListener<? extends Message>> listeners = listenersMap.get(message.getClass());
        if (listeners != null) {
            for (ClientListener<? extends Message> listener : listeners) {
                ((ClientListener<Message>) listener).onMessageReceived(message);
            }
        }
    }

    public void close() {
        try {
            if(this.out != null) this.out.close();
            if(this.in != null) this.in.close();
            if(this.console != null) this.console.close();
            if(!this.socket.isClosed()) this.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
