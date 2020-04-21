package components.background;

import components.utility.ComponentImage;
import components.utility.Resource;
import interfaces.Drawable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.GamePanel.DEBUG_MODE;
import static main.GamePanel.gameSpeed;
import static main.GameWindow.WINDOW_WIDTH;

public class Background implements Drawable {
    private ArrayList<ComponentImage> cloudImages;
    private BufferedImage cloud;

    private int backgroundSpeed = gameSpeed / 3;

    ComponentImage first;
    ComponentImage second;
    ComponentImage third;

    public Background() {
        cloud = new Resource().getResourceImage("/assets/Cloud.png");

        backgroundInit();
    }

    public void backgroundInit() {
        cloudImages = new ArrayList<>();
        cloudImages.add(new ComponentImage(cloud, WINDOW_WIDTH - 700, 40, Color.LIGHT_GRAY));
        cloudImages.add(new ComponentImage(cloud, WINDOW_WIDTH - 400, 20, Color.LIGHT_GRAY));
        cloudImages.add(new ComponentImage(cloud, WINDOW_WIDTH - 200, 80, Color.LIGHT_GRAY));

        first = cloudImages.get(0);
        second = cloudImages.get(1);
        third = cloudImages.get(2);
    }

    @Override
    public void update() {
        first.x -= backgroundSpeed;
        second.x -= backgroundSpeed;
        third.x -= backgroundSpeed;

        if (first.x <= -first.image.getWidth()) {
            first.x = WINDOW_WIDTH;
        }

        if (second.x <= -second.image.getWidth()) {
            second.x = WINDOW_WIDTH;
        }

        if (third.x <= -third.image.getWidth()) {
            third.x = WINDOW_WIDTH;
        }
    }

    @Override
    public void draw(Graphics g) {
        for (ComponentImage clouds : cloudImages) {
            if (DEBUG_MODE) {
                g.setColor(clouds.debugColor);
                g.drawRect(clouds.x, clouds.y, clouds.image.getWidth(), clouds.image.getHeight());
            }
            g.drawImage(clouds.image, clouds.x, clouds.y, null);
        }
    }

    @Override
    public void reset() {
        backgroundInit();
    }
}
