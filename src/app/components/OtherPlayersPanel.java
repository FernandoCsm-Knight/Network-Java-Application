package app.components;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JLabel;
import javax.swing.JPanel;

import app.screens.GameScreen;
import app.services.ScreenWidget;
import common.design.ApplicationFont;
import common.design.ColorPalette;
import common.messages.BoardMoveMessage;

public class OtherPlayersPanel extends ScreenWidget implements Runnable {
    private final AtomicBoolean running;
    private final Map<Integer, GameView> gameViews;
    private final GameScreen screen;

    private int length;

    public JPanel otherPlayers;

    public OtherPlayersPanel(GameScreen screen) {
        this.screen = screen;
        this.running = new AtomicBoolean(true);
        this.gameViews = new HashMap<>();
        this.length = 0;

        setLayout(new BorderLayout());
        setBackground(ColorPalette.LIGHT_GRAY);

        JLabel otherPlayerPanelTitle = new JLabel();
        otherPlayerPanelTitle.setText("Other Players");
        otherPlayerPanelTitle.setHorizontalAlignment(JLabel.CENTER);
        otherPlayerPanelTitle.setFont(ApplicationFont.TITLE_FONT);
     
        this.otherPlayers = new JPanel();
        this.otherPlayers.setLayout(new GridLayout(2, 2));
        this.otherPlayers.setBackground(ColorPalette.LIGHT_GRAY);

        add(otherPlayerPanelTitle, BorderLayout.NORTH);
        add(this.otherPlayers, BorderLayout.CENTER);
    }

    public int getLength() {
        return this.length;
    }

    public void addGame(int id, char[][] board) {
        GameView view = new GameView(board);
        this.gameViews.put(id, view);
        this.otherPlayers.add(view);
        this.otherPlayers.revalidate();
        this.otherPlayers.repaint();
        this.length++;

        if(this.length == 1 && this.screen.canSupportOtherPlayersPanel()) {
            this.screen.setOtherPlayersPanelVisible(true);
        }
    }

    public boolean removeGame(int id) {
        GameView view = this.gameViews.remove(id);
        boolean removed = view != null;

        if(removed) {
            this.otherPlayers.remove(view);
            this.otherPlayers.revalidate();
            this.otherPlayers.repaint();
            this.length--;

            if(this.length == 0) {
                this.screen.setOtherPlayersPanelVisible(false);
            }
        }

        return removed;
    }

    @Override
    public void run() {
        try {
            while(this.running.get()) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                client.getUdpSocket().receive(receivePacket);
        
                BoardMoveMessage boardMove = new BoardMoveMessage();
                DataInputStream in = new DataInputStream(new ByteArrayInputStream(receivePacket.getData()));
                boardMove.read(in);
                this.updateBoard(boardMove);
            }
        } catch (SocketException e) {
            System.out.println("UDP Socket closed.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.shutdown();
        }
    }

    public void updateBoard(BoardMoveMessage boardMove) {
        if(gameViews.get(boardMove.getId()) == null) {
            this.addGame(boardMove.getId(), boardMove.getBoard());
        }

        GameView view = gameViews.get(boardMove.getId());
        view.update(boardMove.getBoard());
    }

    public void shutdown() {
        this.running.set(false);
    }
}
