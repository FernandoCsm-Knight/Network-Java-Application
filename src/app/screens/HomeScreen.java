package app.screens;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import app.components.CustomButton;
import app.components.DevelopersPanel;
import app.components.TitlePanel;
import app.services.ScreenWidget;
import common.design.ApplicationFont;
import common.design.ColorPalette;
import common.integration.ClientListener;
import common.messages.ConnectionMessage;
import common.messages.Message;
import common.messages.PlayMessage;
import common.types.ConnectionState;
import common.types.MessageType;

public class HomeScreen extends ScreenWidget implements ClientListener {

    private JLabel errorLabel = null;
    private JPanel errorPanel = null;
    private CustomButton playButton = null;
    private CustomButton connectButton = null;

    public HomeScreen() {
        setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        headerPanel.setBackground(ColorPalette.LIGHT_GRAY);

        JPanel horizontalPanel = new JPanel();
        horizontalPanel.setLayout(new BoxLayout(horizontalPanel, BoxLayout.X_AXIS));
        horizontalPanel.setBackground(ColorPalette.LIGHT_GRAY);

        JPanel verticalPanel = new JPanel();
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));
        verticalPanel.setBackground(ColorPalette.LIGHT_GRAY);

        ImageIcon icon = new ImageIcon("src/app/resources/settings.png");
        Image resizedImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

        JButton settingsButton = new JButton(new ImageIcon(resizedImage));
        settingsButton.setPreferredSize(new Dimension(40, 40));
        settingsButton.setBackground(ColorPalette.LIGHT_GRAY);
        settingsButton.setBorderPainted(false);
        settingsButton.addActionListener(e -> {
            context.show(navigator, "Settings");
        });

        verticalPanel.add(Box.createVerticalStrut(20));
        verticalPanel.add(settingsButton);

        horizontalPanel.add(verticalPanel);
        horizontalPanel.add(Box.createHorizontalStrut(20));
        headerPanel.add(horizontalPanel);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridBagLayout());
        gridPanel.setBackground(ColorPalette.LIGHT_GRAY);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 0;
        gridPanel.add(new TitlePanel("A Senile Game", "Make a sequence of three!"), gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
        buttonPanel.setBackground(ColorPalette.LIGHT_GRAY);

        CustomButton howToPlayButton = new CustomButton("How to play");
        howToPlayButton.addActionListener(e -> {
            context.show(navigator, "HowToPlay");
        });

        this.playButton = new CustomButton("Play");
        this.playButton.addActionListener(e -> {
            client.send(new PlayMessage());
            context.show(navigator, "Lobby");
        });

        this.connectButton = new CustomButton("Connect");
        this.connectButton.addActionListener(e -> {
            if(client.tryToConnect()) {
                clientThread = new Thread(client);
                clientThread.start();
                
                if(this.errorPanel != null) this.errorPanel.setVisible(false);
            } else if(this.errorLabel != null && this.errorPanel != null) {
                this.errorLabel.setText("Failed to connect to server");
                this.errorPanel.setVisible(true);
            }
        });
        
        buttonPanel.add(howToPlayButton);
        buttonPanel.add(playButton);
        buttonPanel.add(connectButton);
    
        if(client.isConnected()) {
            this.playButton.setVisible(true);
            this.connectButton.setVisible(false);
        } else {
            this.playButton.setVisible(false);
            this.connectButton.setVisible(true);
        }

        gbc.gridy = 1;
        gridPanel.add(buttonPanel, gbc);

        this.errorPanel = new JPanel();
        this.errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));
        this.errorPanel.setBackground(ColorPalette.LIGHT_GRAY);

        this.errorLabel = new JLabel("", SwingConstants.CENTER);
        this.errorLabel.setFont(ApplicationFont.SMALL_FONT);
        this.errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.errorPanel.add(this.errorLabel);

        gbc.gridy = 2;
        gridPanel.add(errorPanel, gbc);
        errorPanel.setVisible(false);

        add(headerPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(new DevelopersPanel(), BorderLayout.SOUTH);

        client.addListener(this);
    }

    @Override
    public void onMessageReceived(Message message) {
        if(message.getType() == MessageType.CONNECTION) {
            ConnectionMessage connectionMessage = (ConnectionMessage) message;
            System.out.println("HomeScreen: " + connectionMessage.getState());
            
            if(connectionMessage.getState() == ConnectionState.DISCONNECTED) {
                this.errorLabel.setText("Disconnected from server");
                this.errorPanel.setVisible(true);
                this.playButton.setVisible(false);
                this.connectButton.setVisible(true);
            } else {
                this.errorPanel.setVisible(false);
                this.playButton.setVisible(true);
                this.connectButton.setVisible(false);
            }

            revalidate();
            repaint();
        }
    }
}
