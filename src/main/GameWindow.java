package main;

import javax.swing.*;

public class GameWindow {
    private static JFrame mainGameWindow;

    private static final String WINDOW_TITLE = "AWESOME DINO";
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 240;
    private static final boolean WINDOW_RESIZABLE = false;
    private static final boolean WINDOW_ALWAYS_ON_TOP = false;

    public GameWindow() {
        mainGameWindow = new JFrame(WINDOW_TITLE);
        mainGameWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainGameWindow.setResizable(WINDOW_RESIZABLE);

        GamePanel gamePanel = new GamePanel();
        mainGameWindow.add(gamePanel);

        mainGameWindow.addKeyListener(gamePanel);

        mainGameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainGameWindow.setLocationRelativeTo(null);
        mainGameWindow.setVisible(true);
        mainGameWindow.setAlwaysOnTop(WINDOW_ALWAYS_ON_TOP);
    }
}
