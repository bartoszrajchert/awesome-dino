import main.GameWindow;

import javax.swing.*;

public class GameStarter {
    public static void main(String[] args) {
        System.out.println("\nStartup log");
        System.out.println("-----------------------------------------------------");
        SwingUtilities.invokeLater(GameWindow::new);
    }
}
