package server.managers;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import server.handlers.GameHandler;
import server.handlers.PlayerHandler;

public class PlayerStatusManager implements Runnable {

    private final ArrayList<PlayerHandler> players;
    private final AtomicBoolean running;
    private final GameHandler game;

    public PlayerStatusManager(ArrayList<PlayerHandler> players, GameHandler game) {
        this.running = new AtomicBoolean(true);
        this.players = players;
        this.game = game;
    }
    
    @Override
    public void run() {

        while(this.running.get() && this.checkPlayers()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        this.game.shutdown("Player disconnected.");
        this.shutdown();
    }

    private boolean checkPlayers() {
        boolean valid = true;

        for(int i = 0; valid && i < this.players.size(); i++) {
            valid = this.players.get(i).isOnline();
        }

        return valid;
    }

    private void shutdown() {
        this.running.set(false);
    }
    
}
