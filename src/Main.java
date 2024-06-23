
import java.awt.EventQueue;

import app.windows.AppWindow;

public class Main {
    public static void main(String[] args) throws Exception {
        AppWindow window = new AppWindow();
        EventQueue.invokeLater(() -> {
            window.setVisible(true);
        });
    }
}
