package app.components;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import common.design.ColorPalette;

public class RoundedScrollPane extends JScrollPane {
    private int radius;

    public RoundedScrollPane(Component view, int radius) {
        super(view);
        this.radius = radius;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        final Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));

        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        final Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(ColorPalette.GREEN);
        g2.setStroke(new BasicStroke(3));
        g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, radius, radius));

        g2.dispose();
    }

    @Override
    public Insets getInsets() {
        final Insets insets = super.getInsets();
        insets.left += radius / 2;
        insets.right += radius / 2;
        insets.top += radius / 2;
        insets.bottom += radius / 2;
        return insets;
    }
}