package components.obstacles;

import components.utility.Coordinates;
import components.utility.ComponentImage;

import java.awt.*;
import java.awt.image.BufferedImage;

import static components.ground.Ground.GROUND_Y;

public class ObstacleImage {
    private int spaceBehind;

    public Coordinates coordinates;

    private ComponentImage image;

    public ObstacleImage(BufferedImage image, int x) {
        this.image = new ComponentImage(image, x, GROUND_Y - image.getHeight(), Color.red);

        coordinates = new Coordinates(this.image.x, this.image.y, image.getWidth(), image.getHeight());
    }

    public void setSpaceBehind(int spaceBehind) {
        this.spaceBehind = spaceBehind;
    }

    public int getSpaceBehind() {
        return spaceBehind;
    }

    public void setX(int x) {
        this.image.x = x;
        coordinates.x = x;
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
