package server.services;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import common.DynamicQueue;
import common.integration.LogListener;
import common.models.Log;

public class LoggerService implements Runnable {
    
    private final ArrayList<LogListener> listeners;
    private final AtomicBoolean running;
    private final DynamicQueue<Log> queue;

    public LoggerService() {
        this.listeners = new ArrayList<>();
        this.running = new AtomicBoolean(true);
        this.queue = new DynamicQueue<>();
    }

    public void addListener(LogListener listener) {
        this.listeners.add(listener);
    }

    public void info(String message) {
        this.queue.enqueue(new Log(message, Log.Level.INFO));
    }

    public void warn(String message) {
        this.queue.enqueue(new Log(message, Log.Level.WARNING));
    }

    public void error(String message) {
        this.queue.enqueue(new Log(message, Log.Level.ERROR));
    }

    public void debug(String message) {
        this.queue.enqueue(new Log(message, Log.Level.DEBUG));
    }

    @Override
    public void run() {
        while(this.running.get()) {
            try {
                Log message = this.queue.dequeue();

                for(LogListener listener : this.listeners) {
                    listener.onLogReceived(message);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    public void shutdown() {
        this.running.set(false);
    }

}
