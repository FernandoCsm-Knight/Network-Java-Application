package server;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import common.DynamicQueue;
import common.messages.StatusMessage;

public class PlayerManager implements Runnable {

    private static final int PLAYERS_PER_GAME = 2;

    private DynamicQueue<PlayerHandler> queue;
    private ArrayList<PlayerHandler> players;
    private AtomicBoolean running;
    private ExecutorService executor;
    
    public PlayerManager() {
        this.queue = new DynamicQueue<PlayerHandler>();
        this.players = new ArrayList<PlayerHandler>();
        this.executor = Executors.newCachedThreadPool();
        this.running = new AtomicBoolean(false);
    }

    public void addPlayer(Socket socket) {
        PlayerHandler player = new PlayerHandler(socket, queue);
        this.players.add(player);
        this.executor.execute(player);
    }

    public void shutdown() {
        this.running.set(false);
        this.executor.shutdown();
    }

    @Override
    public void run() {
        this.running.set(true);

        while(this.running.get()) {
            try {
                Set<PlayerHandler> arr = new HashSet<>();

                while(arr.size() < PLAYERS_PER_GAME) {
                    PlayerHandler player = this.queue.dequeue();
                    player.send(new StatusMessage("Waiting for another player..."));
                    arr.add(player);

                    for(PlayerHandler p : arr) 
                        if(!p.online) arr.remove(p);
                }

                this.executor.execute(new GameHandler(arr));
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        this.shutdown();
    }

}
