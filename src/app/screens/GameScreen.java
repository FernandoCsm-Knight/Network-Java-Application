package app.screens;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import app.components.CustomButton;
import app.components.Tile;
import app.services.ScreenWidget;
import common.design.ApplicationFont;
import common.design.ColorPalette;
import common.integration.ClientListener;
import common.messages.CancelConnectionMessage;
import common.messages.ConnectionMessage;
import common.messages.Message;
import common.messages.MoveMessage;
import common.messages.StatusMessage;
import common.messages.SymbolMessage;
import common.types.ConnectionState;
import common.types.GameStatus;

public class GameScreen extends ScreenWidget implements ClientListener {

    private Tile[][] buttons = new Tile[3][3];
    private JPanel gridPanel;
    private JLabel statusLabel;
    private String playerSymbol = null;
    private boolean playerTurn = false;

    public GameScreen() {
        setLayout(new BorderLayout());
        setBackground(ColorPalette.LIGHT_GRAY);

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridBagLayout());
        gridPanel.setBackground(ColorPalette.LIGHT_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(ColorPalette.LIGHT_GRAY);

        JLabel titleLabel = new JLabel("Tic Tac Toe", SwingConstants.CENTER);
        titleLabel.setFont(ApplicationFont.TITLE_FONT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(titleLabel);

        gbc.gridy = 0;
        gridPanel.add(titlePanel, gbc);

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridBagLayout());
        gamePanel.setBackground(ColorPalette.LIGHT_GRAY);

        GridBagConstraints gameGbc = new GridBagConstraints();
        gameGbc.insets = new Insets(5, 5, 5, 5);
        gameGbc.gridx = 0;
        gameGbc.gridy = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new Tile("", i, j);
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(e -> {
                    Tile buttonClicked = (Tile) e.getSource();
                    if(buttonClicked.getText().equals("") && playerSymbol != null) {
                        if(this.playerTurn) {
                            buttonClicked.setText(this.playerSymbol);
                            client.send(new MoveMessage(buttonClicked.getRow(), buttonClicked.getCol()));
                            this.playerTurn = false;
                            this.changeStatus("Not your turn");
                        } else {
                            // TODO: what to do if player can't move
                        }

                        checkForWin();
                    }
                });

                gameGbc.gridx = j;
                gameGbc.gridy = i;
                gamePanel.add(buttons[i][j], gameGbc);
            }
        }

        gbc.gridy = 1;
        gridPanel.add(gamePanel, gbc);

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBackground(ColorPalette.LIGHT_GRAY);

        this.statusLabel = new JLabel("Not your turn", SwingConstants.CENTER);
        this.statusLabel.setFont(ApplicationFont.STANDARD_FONT);
        this.statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusPanel.add(this.statusLabel);

        gbc.gridy = 2;
        gridPanel.add(statusPanel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(ColorPalette.LIGHT_GRAY);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

        CustomButton backButton = new CustomButton("Back");
        backButton.addActionListener(e -> {
            client.send(new CancelConnectionMessage());
            context.show(navigator, "Home");
            this.resetBoard();
        });

        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int componentHeight = e.getComponent().getHeight() - buttonPanel.getHeight() - 20; // 20 = insets
                int size = (int)Math.min(componentHeight * 0.2, buttons[0][0].getMaximumSize().width);

                for (Tile[] row : buttons) {
                    for (Tile button : row) {
                        button.setPreferredSize(new Dimension(size, size));
                        button.setFont(new Font("Arial", Font.PLAIN, (int)(size * 0.75)));
                    }
                }
            
                revalidate();
                repaint();
            }
        });

        client.addListener(this);
    }

    private void checkForWin() {
        String winner = null;

        for(int i = 0; i < 3 && winner == null; i++) {
            boolean rowWin = checkRowCol(buttons[i][0], buttons[i][1], buttons[i][2]),
                    colWin = checkRowCol(buttons[0][i], buttons[1][i], buttons[2][i]);

            if(rowWin || colWin) winner = rowWin ? buttons[i][0].getText() : buttons[0][i].getText();
        }

        if(winner == null) {
            boolean diag1Win = checkRowCol(buttons[0][0], buttons[1][1], buttons[2][2]),
                    diag2Win = checkRowCol(buttons[0][2], buttons[1][1], buttons[2][0]);

            if(diag1Win || diag2Win) winner = diag1Win ? buttons[0][0].getText() : buttons[0][2].getText();
        }

        if(winner == null) winner = checkForDraw() ? "Draw" : null;
        if(winner != null) showWinMessage(winner);
    }

    private boolean checkForDraw() {
        boolean draw = true;
        
        for(int i = 0; i < 3 && draw; i++) {
            for(int j = 0; j < 3 && draw; j++) {
                if(buttons[i][j].getText().equals("")) {
                    draw = false;
                }
            }
        }

        return draw;
    }

    private boolean checkRowCol(JButton b1, JButton b2, JButton b3) {
        return !b1.getText().equals("") && b1.getText().equals(b2.getText()) && b2.getText().equals(b3.getText());
    }

    private void showWinMessage(String winner) {
        if(winner.equals("Draw")) {
            JOptionPane.showMessageDialog(this, "It's a draw!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, winner + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }

        resetBoard();
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    private void changeStatus(String status) {
        this.statusLabel.setText(status);
        revalidate();
        repaint();
    }

    @Override
    public void onMessageReceived(Message message) {
        switch(message.getType()) {
            case STATUS:
                StatusMessage statusMessage = (StatusMessage) message;
                if(statusMessage.getGameStatus() == GameStatus.FINISHED) {
                    context.show(navigator, "Lobby");
                    this.resetBoard();
                    this.statusLabel.setText("Not your turn");
                }
                break;

            case SYMBOL:
                this.playerSymbol = "" + ((SymbolMessage) message).getSymbol();
                break;
        
            case TURN:
                this.playerTurn = true;
                this.changeStatus("Your turn");
                break;

            case MOVE:
                MoveMessage moveMessage = (MoveMessage) message;
                buttons[moveMessage.getX()][moveMessage.getY()].setText(playerSymbol.equals("X") ? "O" : "X");
                checkForWin();
                break;

            case CONNECTION:
                ConnectionMessage connectionMessage = (ConnectionMessage) message;
                if(connectionMessage.getState() == ConnectionState.DISCONNECTED) {
                    context.show(navigator, "Home");
                    this.resetBoard();
                    revalidate();
                    repaint();
                }

            default:
                break;
        }
    }

}
