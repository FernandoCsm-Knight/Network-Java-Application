package app.components;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import common.design.ApplicationFont;

public class SquarePanel extends JPanel {

    public final JLabel label;

    public SquarePanel() {
        this(null);
    }

    public SquarePanel(Color color) {
        setPreferredSize(new Dimension(100, 100));
        setMinimumSize(new Dimension(50, 50));
        setMaximumSize(new Dimension(150, 150));
        if(color != null) setBackground(color);

        this.label = new JLabel();
        this.label.setFont(ApplicationFont.BOARD_FONT.deriveFont(this.getWidth() * 0.60f));
        add(label);
    }
}