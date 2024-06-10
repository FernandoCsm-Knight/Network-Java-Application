package app.screens;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import app.components.CustomButton;
import app.services.ScreenWidget;
import common.design.ApplicationFont;
import common.design.ColorPalette;
import common.integration.ClientListener;
import common.messages.CancelConnectionMessage;
import common.messages.ConnectionMessage;
import common.messages.Message;
import common.messages.PlayMessage;
import common.messages.StatusMessage;
import common.types.ConnectionState;
import common.types.GameStatus;
import common.types.MessageType;

public class LobbyScreen extends ScreenWidget implements ClientListener {

    private JLabel titleLabel;
    private CustomButton cancelButton;
    private CustomButton tryAgainButton;

    public LobbyScreen() {
        setLayout(new BorderLayout());
        setBackground(ColorPalette.LIGHT_GRAY);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(ColorPalette.LIGHT_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
        centralPanel.setBackground(ColorPalette.LIGHT_GRAY);

        titleLabel = new JLabel("Starting...", JLabel.CENTER);
        titleLabel.setFont(ApplicationFont.TITLE_FONT);
        titleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
        buttonPanel.setBackground(ColorPalette.LIGHT_GRAY);

        this.cancelButton = new CustomButton("Cancel");
        this.cancelButton.addActionListener(e -> {
            client.send(new CancelConnectionMessage());
            context.show(navigator, "Home");
        });

        this.tryAgainButton = new CustomButton("Try again");
        this.tryAgainButton.addActionListener(e -> {
            client.send(new PlayMessage());
        });

        buttonPanel.add(this.cancelButton);
        buttonPanel.add(this.tryAgainButton);

        centralPanel.add(titleLabel);
        centralPanel.add(Box.createVerticalStrut(10));
        centralPanel.add(buttonPanel);
        
        mainPanel.add(centralPanel, gbc);
        add(mainPanel, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                titleLabel.setFont(titleLabel.getFont().deriveFont(
                    ApplicationFont.standardFontScaler(getHeight(), getWidth(), 48.0f)
                ));

                revalidate();
                repaint();
            }
        });

        client.addListener(this);
    }

    @Override
    public void onMessageReceived(Message message) {
        if(message.getType() == MessageType.GAME_STATUS) {
            StatusMessage statusMessage = (StatusMessage) message;
            
            if(statusMessage.getGameStatus() == GameStatus.PLAYING) {
                context.show(navigator, "Game");
            } else if(statusMessage.getGameStatus() == GameStatus.FINISHED) {
                this.cancelButton.setVisible(false);
                this.tryAgainButton.setVisible(true);

                titleLabel.setText(statusMessage.getStatus());
                revalidate();
                repaint();
            } else if(statusMessage.getGameStatus() == GameStatus.WAITING) {
                this.tryAgainButton.setVisible(false);
                this.cancelButton.setVisible(true);
                
                titleLabel.setText(statusMessage.getStatus());
                revalidate();
                repaint();
            }
        } else if(message.getType() == MessageType.CONNECTION) {
            ConnectionMessage connectionMessage = (ConnectionMessage) message;

            if(connectionMessage.getState() == ConnectionState.DISCONNECTED) {
                context.show(navigator, "Home");
                revalidate();
                repaint();
            }

        }
    }

}
