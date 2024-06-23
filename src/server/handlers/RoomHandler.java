package server.handlers;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import common.DynamicQueue;
import common.messages.BoardMoveMessage;
import common.messages.RoomStatusMessage;
import common.types.GameStatus;
import server.services.ServerService;

public class RoomHandler implements Runnable, Closeable, ServerService {
    
    private static final int MAX_GAMES = 5;

    private static class MoveRequest {
        public final char[][] board;
        public final GameHandler game;

        public MoveRequest(char[][] board, GameHandler game) {
            this.board = board;
            this.game = game;
        }
    }

    private final Set<GameHandler> games;
    private final DynamicQueue<MoveRequest> moves;
    private final AtomicBoolean running;

    private DatagramSocket socket;

    public RoomHandler() {
        this.games = new HashSet<GameHandler>();
        this.running = new AtomicBoolean(true);
        this.moves = new DynamicQueue<MoveRequest>();

        try {
            this.socket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
            this.socket = null;
            this.close();
        }
    }

    public int size() {
        return this.games.size();
    }

    public void add(GameHandler game) {
        if(this.games.size() < MAX_GAMES) {
            this.games.add(game);
        }
    }

    public boolean remove(GameHandler game) {
        logger.info("Removing game (id = " + game.getId() + ")");
        boolean removed = this.games.remove(game);

        if(removed) {
            for(GameHandler g : this.games) {
                g.broadcast(new RoomStatusMessage("Game ended!", GameStatus.FINISHED, game.getId()));
            }
        }

        return removed;
    }

    public void sendMove(char[][] board, GameHandler game) {
        this.moves.enqueue(new MoveRequest(board, game));
    }

    private void broadcast(MoveRequest request) {
        for(GameHandler game : this.games) {
            if(game.getId() != request.game.getId()) {
                ArrayList<PlayerHandler> players = game.getPlayers();

                for(PlayerHandler player : players) {
                    if(player.getUdpPort() != -1) {
                        InetAddress address = player.getSocket().getInetAddress();
                        
                        if(address != null) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            DataOutputStream out = new DataOutputStream(baos);

                            BoardMoveMessage message = new BoardMoveMessage(request.game.getId(), request.board);

                            try {
                                message.write(out);
                                byte[] data = baos.toByteArray();
                                this.socket.send(new DatagramPacket(data, data.length, address, player.getUdpPort()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        while(this.running.get()) {
            try {
                MoveRequest request = this.moves.dequeue();
                if(request != null) this.broadcast(request);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        this.close();
    }

    @Override
    public void close() {
        logger.warn("Closing room handler...");
        this.running.set(false);

        if(this.socket != null && !this.socket.isClosed()) {
            this.socket.close();
        }

        logger.warn("Room handler closed.");
    }
}
