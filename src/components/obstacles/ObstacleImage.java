package components.obstacles;

import components.utility.ComponentImage;

import java.awt.*;
import java.awt.image.BufferedImage;

import static components.ground.Ground.GROUND_Y;

public class ObstacleImage extends ComponentImage {
    private int spaceBehind;

    private Rectangle rectCollision;

    public ObstacleImage(BufferedImage image, int x) {
        super(image, x, GROUND_Y - image.getHeight(), Color.red);

        rectCollision = new Rectangle();
        rectCollision.x = this.x;
        rectCollision.y = y;
        rectCollision.width = image.getWidth();
        rectCollision.height = image.getHeight();
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
