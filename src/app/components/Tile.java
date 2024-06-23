package app.components;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import common.design.ColorPalette;

public class Tile extends JButton {

    private int row;
    private int col;

    public Tile(String text, int row, int col) {
        super(text);
        setFocusPainted(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(ColorPalette.GREEN);
        setForeground(ColorPalette.LIGHT_GRAY);
        setPreferredSize(new Dimension(100, 100));
        setMinimumSize(new Dimension(50, 50));
        setMaximumSize(new Dimension(150, 150));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(ColorPalette.DARK_BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(ColorPalette.GREEN);
            }
        });

        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }
}
