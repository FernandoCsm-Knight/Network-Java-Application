package common.design;

import java.awt.Font;

public class ApplicationFont {
    
    public static final Font STANDARD_FONT = new Font("Serif", Font.PLAIN, 24);
    public static final Font MAIN_TITLE_FONT = new Font("Serif", Font.BOLD, 48);
    public static final Font TITLE_FONT = new Font("Serif", Font.BOLD, 32);
    public static final Font SUBTITLE_FONT = new Font("Serif", Font.PLAIN, 24);
    public static final Font BUTTON_FONT = new Font("Serif", Font.PLAIN, 24);
    public static final Font SMALL_FONT = new Font("Serif", Font.PLAIN, 16);
    public static final Font MONOSPACED_FONT = new Font("Monospaced", Font.PLAIN, 16);
    public static final Font BOARD_FONT = new Font("Arial", Font.PLAIN, 60);

    public static float standardFontScaler(int componentHeight, int componentWidth, float maxSize) {
        return Math.min(maxSize, (Math.min(componentHeight, componentWidth / 1.5f) / 15.0f));
    }
}
