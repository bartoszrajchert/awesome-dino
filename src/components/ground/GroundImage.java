package components.ground;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GroundImage {
    BufferedImage image;
    int x;

    Color debugColor;

    public GroundImage(BufferedImage image, int x, Color debugColor) {
        this.image = image;
        this.x = x;
        this.debugColor = debugColor;
    }
}
