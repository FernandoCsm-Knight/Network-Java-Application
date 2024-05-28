package screens;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import common.integration.ClientListener;
import common.messages.CancelConnectionMessage;
import common.messages.StatusMessage;
import components.ColorPalette;

public class LobbyScreen extends ScreenWidget implements ClientListener<StatusMessage> {

    private JLabel titleLabel;

    public LobbyScreen() {
        setLayout(new BorderLayout());
        setBackground(ColorPalette.LIGHT_GRAY);

        titleLabel = new JLabel("", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));

        JButton backButton = new JButton("Cancel");
        backButton.addActionListener(e -> {
            client.send(new CancelConnectionMessage());
            context.show(navigator, "Home");
        });

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(ColorPalette.LIGHT_GRAY);

        mainPanel.add(titleLabel);
        mainPanel.add(backButton);

        add(mainPanel, BorderLayout.CENTER);
        client.addListener(StatusMessage.class, this);
    }

    @Override
    public void onMessageReceived(StatusMessage message) {
        System.out.println("LobbyScreen: " + message.getStatus());
        titleLabel.setText(message.getStatus());

        revalidate();
        repaint();
    }

}
