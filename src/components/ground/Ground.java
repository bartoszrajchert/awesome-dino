package components.ground;

import interfaces.Drawable;
import components.utility.Resource;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.GamePanel.*;

public class Ground implements Drawable {
    public static final int GROUND_Y = 180;
    private static final int GROUND_Y_IMAGE_OFFSET = -9;
    
    private ArrayList<GroundImage> groundImages;
    private BufferedImage image;

    GroundImage first;
    GroundImage second;

    public Ground() {
        image = new Resource().getResourceImage("/assets/Ground.png");

        groundInit();
    }

    public void groundInit() {
        groundImages = new ArrayList<>();
        groundImages.add(new GroundImage(image, 0, Color.green));
        groundImages.add(new GroundImage(image, groundImages.get(0).image.getWidth(), Color.blue));

        first = groundImages.get(0);
        second = groundImages.get(1);
    }

    /**
     * @see GroundImage#x   Defines also inequalities arising
     *                      from updating x before changing GroundImage possition (I think)
     */
    public void update() {
        first.x -= gameSpeed;
        second.x -= gameSpeed;

        if (first.x <= -first.image.getWidth()) {
            first.x = second.image.getWidth() + second.x;
        }
        if (second.x <= -second.image.getWidth()) {
            second.x = first.image.getWidth() + first.x;
        }
    }

    public void draw(Graphics g) {
        for (GroundImage ground : groundImages) {
            if (DEBUG_MODE) {
                g.setColor(ground.debugColor);
                g.drawLine(ground.x, GROUND_Y, ground.image.getWidth() + ground.x, GROUND_Y);
            }
            g.drawImage(ground.image, ground.x, GROUND_Y + GROUND_Y_IMAGE_OFFSET, null);
        }
    }

    @Override
    public void reset() {
        groundInit();
    }
}
