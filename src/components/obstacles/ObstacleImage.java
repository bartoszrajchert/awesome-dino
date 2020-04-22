package components.obstacles;

import components.utility.CollisionBox;
import components.utility.ComponentImage;

import java.awt.*;
import java.awt.image.BufferedImage;

import static components.ground.Ground.GROUND_Y;

public class ObstacleImage {
    private int spaceBehind;

    public CollisionBox collisionBox;

    private ComponentImage image;

    public ObstacleImage(BufferedImage image, int x) {
        this.image = new ComponentImage(image, x, GROUND_Y - image.getHeight(), Color.red);

        collisionBox = new CollisionBox(this.image.x, this.image.y, image.getWidth(), image.getHeight());
    }

    public void setSpaceBehind(int spaceBehind) {
        this.spaceBehind = spaceBehind;
    }

    public int getSpaceBehind() {
        return spaceBehind;
    }

    public void setX(int x) {
        this.image.x = x;
        collisionBox.x = x;
    }

    public int getX() {
        return image.x;
    }
    public int getY() {
        return image.y;
    }
    public Color getDebugColor() {
        return image.debugColor;
    }

    public BufferedImage getImage() {
        return image.image;
    }
}
