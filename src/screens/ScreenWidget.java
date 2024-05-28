package screens;

import java.awt.CardLayout;
import javax.swing.JPanel;

import client.Client;

public abstract class ScreenWidget extends JPanel {
    public static final CardLayout context = new CardLayout();
    public static final JPanel navigator = new JPanel(context);
    public static Client client;
}
