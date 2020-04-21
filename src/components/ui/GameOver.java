package components.ui;

import interfaces.Drawable;
import components.utility.Resource;
import components.utility.Sound;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GameWindow.WINDOW_HEIGHT;
import static main.GameWindow.WINDOW_WIDTH;

public class GameOver implements Drawable {
    private final BufferedImage text = new Resource().getResourceImage("/assets/Game-over.png");
    private final BufferedImage restartButton = new Resource().getResourceImage("/assets/Restart.png");
    private final Sound gameOverSound = new Sound("/assets/sounds/hit.wav");

    public GameOver() { }

    @Override
    public void update() { }

    @Override
    public void draw(Graphics g) {
        g.drawImage(text, (WINDOW_WIDTH - text.getWidth())/ 2, (WINDOW_HEIGHT - text.getHeight()) / 2 - 70, null);
        g.drawImage(restartButton, (WINDOW_WIDTH - restartButton.getWidth())/ 2, (WINDOW_HEIGHT - restartButton.getHeight()) / 2 - 30, null);

        gameOverSound.play();
    }

    @Override
    public void reset() { }
}
