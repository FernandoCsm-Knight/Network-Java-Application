package app.components;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import common.design.ApplicationFont;
import common.design.ColorPalette;

public class TitlePanel extends JPanel {

    private String title;
    private String subtitle;

    public TitlePanel() {
        this(null, null);   
    }

    public TitlePanel(String title) {
        this(title, null);
    }

    public TitlePanel(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(ColorPalette.LIGHT_GRAY);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(ApplicationFont.MAIN_TITLE_FONT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(subtitle, SwingConstants.CENTER);
        subtitleLabel.setFont(ApplicationFont.SUBTITLE_FONT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(titleLabel);
        add(Box.createVerticalStrut(10));
        add(subtitleLabel);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
        revalidate();
        repaint();
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        revalidate();
        repaint();
    }
}
