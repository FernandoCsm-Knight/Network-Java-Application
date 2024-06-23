import java.awt.EventQueue;

import app.windows.LogWindow;
import common.types.IPVersion;
import server.Server;

public class DeamonIPv4 {
    public static void main(String[] args) {
        LogWindow logScreen = new LogWindow();
        EventQueue.invokeLater(() -> {
            logScreen.setVisible(true);
        });

        Server server = null;

        try {
            server = new Server(IPVersion.IPv4, "127.0.0.1", 8081);
            server.start();
        } catch(Exception e) {
            if(server != null) server.shutdown();
            e.printStackTrace();
        }
    }
}
