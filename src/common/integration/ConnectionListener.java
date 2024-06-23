package common.integration;

import java.util.concurrent.atomic.AtomicBoolean;

import client.Client;
import common.messages.ConnectionMessage;
import common.types.ConnectionState;

public class ConnectionListener implements Runnable {

    private boolean lastStatus = false;

    private final Client client;
    private final AtomicBoolean running;

    public ConnectionListener(Client client) {
        this.client = client;
        this.running = new AtomicBoolean(true);
    }

    @Override
    public void run() {
        this.running.set(true);

        while(this.running.get()) {
            if(this.client.isConnected() != lastStatus) {
                lastStatus = this.client.isConnected();
                ConnectionState state = (lastStatus) ? ConnectionState.CONNECTED : ConnectionState.DISCONNECTED;
                this.client.notifyListeners(new ConnectionMessage(state));
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void shutdown() {
        this.running.set(false);
    }
}
