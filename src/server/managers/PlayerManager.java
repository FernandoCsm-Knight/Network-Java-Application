package server.managers;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import common.DynamicQueue;
import common.messages.StatusMessage;
import common.types.GameStatus;
import server.handlers.GameHandler;
import server.handlers.PlayerHandler;
import server.handlers.RoomHandler;
import server.services.ServerService;

public class PlayerManager implements Runnable, ServerService {

    private static final int PLAYERS_PER_GAME = 2;
    
    private final ExecutorService executor;
    private final DynamicQueue<PlayerHandler> queue;
    private final Set<PlayerHandler> playerSet;
    private final Set<PlayerHandler> players;
    private final AtomicBoolean running;
    private final RoomHandler room;

    public PlayerManager() {
        this.queue = new DynamicQueue<PlayerHandler>();
        this.playerSet = new HashSet<PlayerHandler>();
        this.players = new HashSet<PlayerHandler>();
        this.executor = Executors.newCachedThreadPool();
        this.running = new AtomicBoolean(false);
        this.room = new RoomHandler();

        this.executor.execute(this.room);
    }

    public void addClient(Socket socket) {
        PlayerHandler player = new PlayerHandler(socket, this);
        this.players.add(player);
        this.executor.execute(player);
    }

    public void connect(PlayerHandler player) {
        this.queue.enqueue(player);
    }

    public void cancelConnection(PlayerHandler player) {
        boolean removed = this.queue.remove(player);
        if(!removed) this.playerSet.remove(player);
        if(player.game != null) this.room.remove(player.game);
        this.players.remove(player);
    }

    @Override
    public void run() {
        this.running.set(true);

        while(this.running.get()) {
            try {
                while(this.playerSet.size() < PLAYERS_PER_GAME) {
                    PlayerHandler player = this.queue.dequeue();
                    player.send(new StatusMessage("Waiting for another player...", GameStatus.WAITING));
                    this.playerSet.add(player);
                }

                logger.info("Starting game with " + this.playerSet.size() + " players.");
                GameHandler game = new GameHandler(new ArrayList<PlayerHandler>(this.playerSet), this.room);
                this.room.add(game);
                this.executor.execute(game);
                this.playerSet.clear();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        this.shutdown();
    }

    public void shutdown() {
        this.running.set(false);
        this.executor.shutdown();
    }

}
