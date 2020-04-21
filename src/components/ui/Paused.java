package components.ui;

import components.utility.Resource;
import interfaces.Drawable;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GameWindow.WINDOW_HEIGHT;
import static main.GameWindow.WINDOW_WIDTH;

public class Paused implements Drawable {
    private final BufferedImage text = new Resource().getResourceImage("/assets/Paused.png");

    public Paused() {

    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics g) {
        System.out.println("draw pause");
        g.drawImage(text, (WINDOW_WIDTH - text.getWidth())/ 2, (WINDOW_HEIGHT - text.getHeight()) / 2 - 70, null);
    }

    @Override
    public void reset() {

    }
}
