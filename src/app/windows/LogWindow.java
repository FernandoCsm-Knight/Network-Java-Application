package app.windows;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import app.components.RoundedScrollPane;
import common.design.ApplicationFont;
import common.design.ColorPalette;
import common.integration.LogListener;
import common.models.Log;
import server.services.ServerService;

public class LogWindow extends JFrame implements LogListener, ServerService {

    private JTextArea logArea;

    public LogWindow() {
        setTitle("System Logs");
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon("src/app/resources/log.png");
        setIconImage(icon.getImage());

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(ColorPalette.LIGHT_GRAY);

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;

        final JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(ColorPalette.LIGHT_GRAY);

        final JLabel titleLabel = new JLabel("System Logs", SwingConstants.CENTER);
        titleLabel.setFont(ApplicationFont.TITLE_FONT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(titleLabel);

        gbc.gridy = 0;
        mainPanel.add(titlePanel, gbc);

        logArea = new JTextArea();
        logArea.setFont(ApplicationFont.MONOSPACED_FONT);
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBackground(ColorPalette.WHITE);
        logArea.setMargin(new Insets(10, 10, 10, 10));

        final RoundedScrollPane scrollPane = new RoundedScrollPane(logArea, 20);
        scrollPane.setBackground(ColorPalette.WHITE);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        mainPanel.add(scrollPane, gbc);

        add(mainPanel, BorderLayout.CENTER);
        logger.addListener(this);
    }

    @Override
    public void onLogReceived(Log log) {
        logArea.append(log + "\n");
    }
}
