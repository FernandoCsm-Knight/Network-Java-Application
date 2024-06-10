package app.windows;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import app.screens.GameScreen;
import app.screens.HomeScreen;
import app.screens.HowToPlayScreen;
import app.screens.LobbyScreen;
import app.screens.SettingsScreen;
import app.services.ScreenWidget;
import common.integration.ConnectionListener;
import common.messages.UdpConfigMessage;

public class AppWindow extends JFrame {

    public AppWindow() {
        setTitle("A Senile Game");
        setResizable(true);
        setSize(800, 600);
        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon("src/app/resources/icon.png");
        setIconImage(icon.getImage());
        
        ScreenWidget.connectionListener = new ConnectionListener(ScreenWidget.client);
        ScreenWidget.connectionThread = new Thread(ScreenWidget.connectionListener);
        ScreenWidget.connectionThread.start();

        if(ScreenWidget.client.tryToConnect()) {
            ScreenWidget.clientThread = new Thread(ScreenWidget.client);
            ScreenWidget.clientThread.start();

            if(ScreenWidget.client.isUdpConnected()) {
                ScreenWidget.client.send(new UdpConfigMessage(ScreenWidget.client.getUdpSocket().getLocalPort()));
            }
        }

        ScreenWidget.navigator.add(new HomeScreen(), "Home");
        ScreenWidget.navigator.add(new HowToPlayScreen(), "HowToPlay");
        ScreenWidget.navigator.add(new GameScreen(), "Game");
        ScreenWidget.navigator.add(new LobbyScreen(), "Lobby");
        ScreenWidget.navigator.add(new SettingsScreen(), "Settings");

        add(ScreenWidget.navigator);
    }

    @Override
    public void dispose() {
        ScreenWidget.close();
        super.dispose();
    }
}

