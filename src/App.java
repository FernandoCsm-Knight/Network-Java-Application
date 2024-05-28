import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import client.Client;
import screens.GameScreen;
import screens.HomeScreen;
import screens.HowToPlayScreen;
import screens.LobbyScreen;
import screens.ScreenWidget;

public class App  extends JFrame {

    private Thread clientThread;

    public App() {
        setTitle("A Senile Game");
        setResizable(true);
        setSize(800, 600);
        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon("src/resources/icon.png");
        setIconImage(icon.getImage());

        try {
            ScreenWidget.client = new Client("localhost", 8080);
            clientThread = new Thread(ScreenWidget.client);
            clientThread.start();
        } catch (Exception e) {
            ScreenWidget.client = null;
            e.printStackTrace();
        }

        ScreenWidget.navigator.add(new HomeScreen(), "Home");
        ScreenWidget.navigator.add(new HowToPlayScreen(), "HowToPlay");
        ScreenWidget.navigator.add(new GameScreen(), "Play");
        ScreenWidget.navigator.add(new LobbyScreen(), "Lobby");

        add(ScreenWidget.navigator);
    }

    public static void main(String[] args) throws Exception {
        App app = new App();
        EventQueue.invokeLater(() -> {
            app.setVisible(true);
        });
    }
}
