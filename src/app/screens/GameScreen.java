package app.screens;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import app.components.CustomButton;
import app.components.Game;
import app.components.OtherPlayersPanel;
import app.services.ScreenWidget;
import common.design.ColorPalette;
import common.integration.ClientListener;
import common.messages.CancelConnectionMessage;
import common.messages.ConnectionMessage;
import common.messages.Message;
import common.messages.MoveMessage;
import common.messages.PlayerStatusMessage;
import common.messages.RoomStatusMessage;
import common.messages.StatusMessage;
import common.messages.SymbolMessage;
import common.messages.TurnMessage;
import common.types.ConnectionState;
import common.types.GameStatus;

public class GameScreen extends ScreenWidget implements ClientListener {

    private Game game;
    private OtherPlayersPanel otherPlayersPanel;
    private Thread otherPlayersPanelThread;
    private JPanel mainPanel;

    public GameScreen() {
        setLayout(new BorderLayout());
        setBackground(ColorPalette.LIGHT_GRAY);

        this.game = new Game();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(ColorPalette.LIGHT_GRAY);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

        CustomButton backButton = new CustomButton("Back");
        backButton.addActionListener(e -> {
            client.send(new CancelConnectionMessage());
            context.show(navigator, "Home");
            this.game.resetBoard();
        });

        buttonPanel.add(backButton);

        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new GridLayout(1, 2));
        this.mainPanel.setBackground(ColorPalette.LIGHT_GRAY);

        this.mainPanel.add(this.game);

        this.otherPlayersPanel = new OtherPlayersPanel(this);
        this.otherPlayersPanelThread = new Thread(this.otherPlayersPanel);
        this.otherPlayersPanelThread.start();

        add(buttonPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if(otherPlayersPanel.getLength() > 0) {
                    setOtherPlayersPanelVisible(canSupportOtherPlayersPanel());
                }
            }
        });

        client.addListener(this);
    }

    public boolean canSupportOtherPlayersPanel() {
        return getWidth() >= 1000 && getHeight() >= 700;
    }

    public void setOtherPlayersPanelVisible(boolean visible) {
        if(visible) {
            if (this.mainPanel.getComponentCount() == 1) {
                this.mainPanel.setLayout(new GridLayout(1, 2));
                this.mainPanel.add(otherPlayersPanel);
                this.mainPanel.revalidate();
                this.mainPanel.repaint();
            }
        } else {
            if (this.mainPanel.getComponentCount() == 2) {
                this.mainPanel.remove(otherPlayersPanel);
                this.mainPanel.setLayout(new GridLayout(1, 1));
                this.mainPanel.revalidate();
                this.mainPanel.repaint();
            }
        }
    }

    public void showWinMessage(String winner) {
        JOptionPane.showMessageDialog(this, winner, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onMessageReceived(Message message) {
        switch(message.getType()) {
            case GAME_STATUS:
                StatusMessage statusMessage = (StatusMessage) message;
                
                if(statusMessage.getGameStatus() == GameStatus.FINISHED) {
                    context.show(navigator, "Lobby");
                    this.game.resetBoard();
                } else if(statusMessage.getId() >= 0) {
                    this.game.setId(statusMessage.getId());
                }

                break;

            case ROOM_STATUS:
                RoomStatusMessage roomStatus = (RoomStatusMessage) message;

                if(roomStatus.getGameStatus() == GameStatus.FINISHED) {
                    this.otherPlayersPanel.removeGame(roomStatus.getId());
                }

                break;

            case PLAYER_STATUS:
                PlayerStatusMessage playerStatus = (PlayerStatusMessage) message;
                this.showWinMessage(playerStatus.getStatus());
                this.game.resetBoard();
                break;

            case SYMBOL:
                this.game.setPlayerSymbol("" + ((SymbolMessage) message).getSymbol());
                break;
        
            case TURN:
                TurnMessage turnMessage = (TurnMessage) message;
                this.game.setTurn(turnMessage.getTurn());
                break;

            case MOVE:
                MoveMessage moveMessage = (MoveMessage) message;
                this.game.otherPlayerMove(moveMessage.getX(), moveMessage.getY());
                break;

            case CONNECTION:
                ConnectionMessage connectionMessage = (ConnectionMessage) message;
                if(connectionMessage.getState() == ConnectionState.DISCONNECTED) {
                    context.show(navigator, "Home");
                    this.game.resetBoard();
                    revalidate();
                    repaint();
                }
                break;

            default:
        }
    }
}
