import java.awt.EventQueue;

import app.windows.LogWindow;
import common.types.IPVersion;
import server.Server;

public class DeamonIPv6 {
    public static void main(String[] args) {
        LogWindow logScreen = new LogWindow();
        EventQueue.invokeLater(() -> {
            logScreen.setVisible(true);
        });

        Server server = null;

        try {
            server = new Server(IPVersion.IPv6, "::1", 8080);
            server.start();
        } catch(Exception e) {
            if(server != null) server.shutdown();
            e.printStackTrace();
        }
    }
}
