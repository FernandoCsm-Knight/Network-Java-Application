package components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;


public class CustomButton extends JButton {

    public CustomButton(String text) {
        this(text, null);
    }

    public CustomButton(String text, Icon icon) {
        super(text, icon);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(ColorPalette.GREEN);
        setForeground(ColorPalette.LIGHT_GRAY);
        setPreferredSize(new Dimension(150, 50));

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
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 5, 5));
        super.paintComponent(g);
        g2.dispose();
    }
}