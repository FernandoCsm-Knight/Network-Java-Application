package screens;

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

import components.ColorPalette;
import components.CustomButton;
import components.Tile;

public class GameScreen extends ScreenWidget {

    private Tile[][] buttons = new Tile[3][3];
    private JPanel gridPanel;
    private boolean playerXTurn = true;

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
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
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
                buttons[i][j] = new Tile("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(e -> {
                    JButton buttonClicked = (JButton) e.getSource();
                    if (buttonClicked.getText().equals("")) {
                        if (playerXTurn) {
                            buttonClicked.setText("X");
                        } else {
                            buttonClicked.setText("O");
                        }
                        playerXTurn = !playerXTurn;
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

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(ColorPalette.LIGHT_GRAY);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

        CustomButton backButton = new CustomButton("Back");
        backButton.addActionListener(e -> {
            context.show(navigator, "Home");
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
    }

    private void checkForWin() {
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(buttons[i][0], buttons[i][1], buttons[i][2]) ||
                checkRowCol(buttons[0][i], buttons[1][i], buttons[2][i])) {
                showWinMessage();
                return;
            }
        }
        if (checkRowCol(buttons[0][0], buttons[1][1], buttons[2][2]) ||
            checkRowCol(buttons[0][2], buttons[1][1], buttons[2][0])) {
            showWinMessage();
        }
    }

    private boolean checkRowCol(JButton b1, JButton b2, JButton b3) {
        return !b1.getText().equals("") && b1.getText().equals(b2.getText()) && b2.getText().equals(b3.getText());
    }

    private void showWinMessage() {
        String winner = playerXTurn ? "O" : "X";
        JOptionPane.showMessageDialog(this, "Jogador " + winner + " venceu!");
        resetBoard();
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        playerXTurn = true;
    }


}
