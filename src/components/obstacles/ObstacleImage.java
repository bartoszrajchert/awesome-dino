package components.obstacles;

import java.awt.*;
import java.awt.image.BufferedImage;

import static components.ground.Ground.GROUND_Y;

public class ObstacleImage {
    BufferedImage image;
    int x;
    int y;
    private int spaceBehind;

    Color debugColor;

    private Rectangle rectCollision;

    public ObstacleImage(BufferedImage image, int x) {
        this.image = image;
        this.x = x;
        y = GROUND_Y - image.getHeight();

        debugColor = Color.red;

        rectCollision = new Rectangle();
        rectCollision.x = this.x;
        rectCollision.y = y;
        rectCollision.width = image.getWidth();
        rectCollision.height = image.getHeight();
    }

    public ObstacleImage(BufferedImage image, int x, Color debugColor) {
        this.image = image;
        this.x = x;
        y = GROUND_Y - image.getHeight();

        this.debugColor = debugColor;
    }

    public void setSpaceBehind(int spaceBehind) {
        this.spaceBehind = spaceBehind;
    }

    public int getSpaceBehind() {
        return spaceBehind;
    }

    public void setX(int x) {
        this.x = x;
        rectCollision.x = x;
    }

    public Rectangle getRectCollision() {
        return rectCollision;
    }
}
