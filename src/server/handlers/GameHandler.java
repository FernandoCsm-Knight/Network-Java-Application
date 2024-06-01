package server.handlers;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import common.messages.Message;
import common.messages.MoveMessage;
import common.messages.StatusMessage;
import common.messages.SymbolMessage;
import common.messages.TurnMessage;
import common.types.GameStatus;
import server.managers.PlayerStatusManager;
import server.services.ServerService;

public class GameHandler implements Runnable, ServerService {

    private static final char[] symbols = {'X', 'O'};

    private Thread playerStatusThread;
    private final ArrayList<PlayerHandler> players;
    private final AtomicBoolean running;

    public GameHandler(Set<PlayerHandler> players) {
        this.players = new ArrayList<PlayerHandler>(players);
        this.running = new AtomicBoolean(true);
    }
    
    @Override
    public void run() {
        this.broadcast(new StatusMessage("Game started!", GameStatus.PLAYING));

        playerStatusThread = new Thread(new PlayerStatusManager(this.players, this));
        playerStatusThread.start();

        final Random random = new Random();
        int turn = random.nextInt(2);

        players.get(turn).send(new SymbolMessage(symbols[turn]));
        players.get((turn + 1) % 2).send(new SymbolMessage(symbols[(turn + 1) % 2]));

        while(this.running.get()) {
            PlayerHandler player = this.players.get(turn);
            player.send(new TurnMessage());

            try {
                MoveMessage move = (MoveMessage) player.receive();

                while(move.getX() != -1 && move.getY() != -1) {
                    turn = (turn + 1) % 2;
                    player = this.players.get(turn);
                    player.send(move);
                    player.send(new TurnMessage());
                    move = (MoveMessage) player.receive();
                }

                if(move.getX() == -1 && move.getY() == -1) {
                    this.shutdown("Game finished!");
                }
            } catch (InterruptedException e) {
                logger.error("Error receiving move");
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

            turn = (turn + 1) % 2;
        }
    }

    public void broadcast(Message message) {
        for(PlayerHandler player : this.players) {
            if(player.isOnline()) player.send(message);
        }
    }

    public void shutdown(String status) {
        this.broadcast(new StatusMessage(status, GameStatus.FINISHED));
        this.running.set(false);
        logger.info("Game finished!");
        
        if(!Thread.interrupted()) {
            Thread.currentThread().interrupt();
        }
    }
}
