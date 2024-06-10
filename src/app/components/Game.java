package app.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import app.services.ScreenWidget;
import common.design.ApplicationFont;
import common.design.ColorPalette;
import common.messages.MoveMessage;

public class Game extends JPanel {

    private Tile[][] buttons = new Tile[3][3];
    private String playerSymbol = null;
    private boolean playerTurn = false;
    private JLabel statusLabel;
    private int id;
    
    public Game() {
        setLayout(new GridBagLayout());
        setBackground(ColorPalette.LIGHT_GRAY);

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
        add(titlePanel, gbc);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridBagLayout());
        gridPanel.setBackground(ColorPalette.LIGHT_GRAY);

        GridBagConstraints gameGbc = new GridBagConstraints();
        gameGbc.insets = new Insets(5, 5, 5, 5);
        gameGbc.gridx = 0;
        gameGbc.gridy = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new Tile("", i, j);
                buttons[i][j].setFont(ApplicationFont.BOARD_FONT);
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(e -> {
                    Tile buttonClicked = (Tile) e.getSource();
                    if(buttonClicked.getText().equals("") && playerSymbol != null) {
                        if(this.playerTurn) {
                            buttonClicked.setText(this.playerSymbol);
                            ScreenWidget.client.send(new MoveMessage(buttonClicked.getRow(), buttonClicked.getCol()));
                            this.setTurn(false);
                        } else {
                            // TODO: what to do if player can't move
                        }
                    }
                });

                gameGbc.gridx = j;
                gameGbc.gridy = i;
                gridPanel.add(buttons[i][j], gameGbc);
            }
        }

        gbc.gridy = 1;
        add(gridPanel, gbc);

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBackground(ColorPalette.LIGHT_GRAY);

        this.statusLabel = new JLabel("Not your turn", SwingConstants.CENTER);
        this.statusLabel.setFont(ApplicationFont.STANDARD_FONT);
        this.statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusPanel.add(this.statusLabel);

        gbc.gridy = 2;
        add(statusPanel, gbc);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateSize(e.getComponent().getWidth(), e.getComponent().getHeight());
            }
        });
    }

    private void changeStatus(String status) {
        this.statusLabel.setText(status);
        revalidate();
        repaint();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setTurn(boolean turn) {
        this.playerTurn = turn;
        this.changeStatus(turn ? "Your turn" : "Not your turn");
    }

    public void setPlayerSymbol(String symbol) {
        this.playerSymbol = symbol;
    }

    public void otherPlayerMove(int row, int col) {
        this.buttons[row][col].setText(playerSymbol.equals("X") ? "O" : "X");
    }

    public void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    public void updateSize(int width, int height) {
        int size;
        height = (int)((height / 3 - 50) * 0.8);
        width = width / 3 - 50;

        if(width < height) {
            size = Math.min(width, buttons[0][0].getMaximumSize().width);
        } else {
            size = (int)Math.min(height, buttons[0][0].getMaximumSize().width);
        }
    
        for (Tile[] row : buttons) {
            for (Tile button : row) {
                button.setPreferredSize(new Dimension(size, size));
                button.setFont(button.getFont().deriveFont(buttons[0][0].getWidth() * 0.60f));
            }
        }

        revalidate();
        repaint();
    }
}
