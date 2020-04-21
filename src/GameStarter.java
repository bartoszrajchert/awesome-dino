import main.GameWindow;

import javax.swing.*;

public class GameStarter {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameWindow();
        });
    }
}
