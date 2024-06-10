package server.handlers;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import common.messages.Message;
import common.messages.MoveMessage;
import common.messages.PlayerStatusMessage;
import common.messages.StatusMessage;
import common.messages.TurnMessage;
import common.types.GameStatus;
import common.types.PlayerStatus;
import server.managers.PlayerStatusManager;
import server.services.ServerService;

public class GameHandler implements Runnable, ServerService {

    private static final char[] symbols = {'X', 'O'};
    private static int ID = 0;

    private Thread playerStatusThread;
    private final PlayerStatusManager playerStatusManager;
    private final ArrayList<PlayerHandler> players;
    private final AtomicBoolean running;
    private final RoomHandler room;
    private final char[][] board;
    private final int id;

    public GameHandler(ArrayList<PlayerHandler> players, RoomHandler room) {
        this.board = new char[3][3];
        this.players = players;
        this.room = room;
        this.id = ID++;
        this.running = new AtomicBoolean(true);
        this.playerStatusManager = new PlayerStatusManager(players, this);

        for(PlayerHandler player : this.players) player.game = this;
        this.resetBoard();
    }

    public ArrayList<PlayerHandler> getPlayers() {
        return this.players;
    }

    public char[][] getBoard() {
        return this.board;
    }
    
    public int getId() {
        return this.id;
    }

    @Override
    public void run() {
        this.broadcast(new StatusMessage("Game started!", GameStatus.PLAYING, this.id));

        playerStatusThread = new Thread(this.playerStatusManager);
        playerStatusThread.start();

        final Random random = new Random();
        int turn = random.nextInt(2);

        players.get(turn).setSymbol(symbols[turn]);
        players.get((turn + 1) % 2).setSymbol(symbols[(turn + 1) % 2]);

        while(this.running.get()) {
            PlayerHandler player = this.players.get(turn);
            player.send(new TurnMessage(true));

            try {
                boolean finished = false;

                do {
                    MoveMessage move = (MoveMessage) player.receive();

                    this.board[move.getX()][move.getY()] = player.getSymbol();
                    this.room.sendMove(this.board, this);

                    turn = (turn + 1) % 2;
                    player = this.players.get(turn);
                    player.send(move);

                    finished = this.checkForWin();

                    if(!finished) player.send(new TurnMessage(true));
                } while(!finished);

                this.resetBoard();

            } catch (InterruptedException e) {
                logger.error("Error receiving move");
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean checkForWin() {
        Character winner = null;

        for(int i = 0; i < 3 && winner == null; i++) {
            if(this.board[i][0] == this.board[i][1] && this.board[i][1] == this.board[i][2] && this.board[i][0] != '\0') {
                winner = this.board[i][0];
            }

            if(this.board[0][i] == this.board[1][i] && this.board[1][i] == this.board[2][i] && this.board[0][i] != '\0') {
                winner = this.board[0][i];
            }
        }

        if(this.board[0][0] == this.board[1][1] && this.board[1][1] == this.board[2][2] && this.board[0][0] != '\0') {
            winner = this.board[0][0];
        }

        if(this.board[0][2] == this.board[1][1] && this.board[1][1] == this.board[2][0] && this.board[0][2] != '\0') {
            winner = this.board[0][2];
        }

        if(winner == null) winner = this.checkForDraw() ? 'D' : null;
        return testWinner(winner);
    }

    private boolean checkForDraw() {
        boolean draw = true;
        
        for(int i = 0; i < 3 && draw; i++) {
            for(int j = 0; j < 3 && draw; j++) {
                if(this.board[i][j] == '\0') {
                    draw = false;
                }
            }
        }

        return draw;
    }

    private boolean testWinner(Character winner) {
        boolean finished = winner != null;

        if(finished) {
            if(winner == 'D') {
                this.broadcast(new PlayerStatusMessage("it's a draw!", PlayerStatus.DRAW));
            } else {
                for(PlayerHandler player : this.players) {
                    if(player.getSymbol() == winner) {
                        player.send(new PlayerStatusMessage("You won!", PlayerStatus.WIN));
                    } else {
                        player.send(new PlayerStatusMessage("You lost!", PlayerStatus.LOSE));
                    }
                }
            }
        }

        return finished;
    }

    private void resetBoard() {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                this.board[i][j] = '\0';
            }
        }

        this.broadcast(new TurnMessage(false));
    }

    public void broadcast(Message message) {
        for(PlayerHandler player : this.players) {
            if(player.isOnline()) player.send(message);
        }
    }

    public void shutdown(String status) {
        this.broadcast(new StatusMessage(status, GameStatus.FINISHED));
        this.running.set(false);
        this.playerStatusManager.shutdown();
        this.room.remove(this);
        for(PlayerHandler player : this.players) player.game = null;
        logger.info("Game finished!");
        
        if(!Thread.interrupted()) {
            Thread.currentThread().interrupt();
        }
    }
}
