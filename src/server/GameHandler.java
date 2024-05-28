package server;

import java.util.Set;

import common.messages.Message;
import common.messages.StatusMessage;

public class GameHandler implements Runnable {

    private Set<PlayerHandler> players;

    public GameHandler(Set<PlayerHandler> players) {
        this.players = players;
    }
    
    @Override
    public void run() {
        this.broadcast(new StatusMessage("Game started!"));
    }

    public void broadcast(Message message) {
        for(PlayerHandler player : this.players) {
            player.send(message);
        }
    }

}
