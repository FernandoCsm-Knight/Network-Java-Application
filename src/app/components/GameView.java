package app.components;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import common.design.ColorPalette;

public class GameView extends JPanel {

    private SquarePanel[][] tiles;

    public GameView(char[][] board) {
        this.tiles = new SquarePanel[board.length][board[0].length];
        setLayout(new GridBagLayout());
        setBackground(ColorPalette.LIGHT_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                SquarePanel tile = new SquarePanel(ColorPalette.GREEN);
                tiles[i][j] = tile;
                gbc.gridx = j;
                gbc.gridy = i;
                add(tile, gbc);
            }
        }
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateSize(e.getComponent().getWidth(), e.getComponent().getHeight());
            }
        });
    }

    public void update(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] == '\0') tiles[i][j].label.setText(null);
                else tiles[i][j].label.setText("" + board[i][j]);
            }
        }

        revalidate();
        repaint();
    }

    public void updateSize(int width, int height) {
        int size;
        height = (int)((height / 3 - 25) * 0.8);
        width = width / 3  - 25;

        if(width < height) {
            size = Math.min(width, tiles[0][0].getMaximumSize().width);
        } else {
            size = (int)Math.min(height, tiles[0][0].getMaximumSize().width);
        }
    
        for (SquarePanel[] row : tiles) {
            for (SquarePanel element : row) {
                element.setPreferredSize(new Dimension(size, size));
                element.label.setFont(element.label.getFont().deriveFont(size * 0.60f));
            }
        }

        revalidate();
        repaint();
    }
}
