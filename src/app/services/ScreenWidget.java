package app.services;

import java.awt.CardLayout;
import javax.swing.JPanel;

import client.Client;
import common.integration.ConnectionListener;

public abstract class ScreenWidget extends JPanel {
    public static final CardLayout context = new CardLayout();
    public static final JPanel navigator = new JPanel(context);
    public static final Client client = new Client();
    public static ConnectionListener connectionListener;
    public static Thread connectionThread;
    public static Thread clientThread;

    public static void close() {
        client.close();
        connectionListener.shutdown();
    }
}
