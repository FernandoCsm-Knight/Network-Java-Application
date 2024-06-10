package app.components;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import common.design.ApplicationFont;
import common.design.ColorPalette;

public class DevelopersPanel extends JPanel {
    
    public DevelopersPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(800, 60));
        setBackground(ColorPalette.LIGHT_GRAY);

        JLabel editorLabel = new JLabel("Made by Augusto S. Oliveira | Pedro H. Pena | Fernando C. Maria", SwingConstants.CENTER);
        editorLabel.setFont(ApplicationFont.SMALL_FONT);
        editorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        add(editorLabel);
    }

}
