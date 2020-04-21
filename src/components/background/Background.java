package components.background;

import components.utility.ComponentImage;
import components.utility.Resource;
import interfaces.Drawable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.GamePanel.*;
import static main.GameWindow.WINDOW_HEIGHT;
import static main.GameWindow.WINDOW_WIDTH;

public class Background implements Drawable {
    private ArrayList<ComponentImage> cloudImages;
    private BufferedImage cloud;

    private int backgroundSpeed = gameSpeed / 3;

    private static BackgroundColors whichBackgroundColor;

    ComponentImage firstCloud;
    ComponentImage secondCloud;
    ComponentImage thirdCloud;

    public Background() {
        cloud = new Resource().getResourceImage("/assets/Cloud.png");
        whichBackgroundColor = BackgroundColors.DEFAULT;

        backgroundInit();
    }

    public static void setWhichBackgroundColor(BackgroundColors whichBackgroundColor) {
        Background.whichBackgroundColor = whichBackgroundColor;
    }

    private void backgroundInit() {
        cloudImages = new ArrayList<>();
        cloudImages.add(new ComponentImage(cloud, WINDOW_WIDTH - 700, 40, Color.LIGHT_GRAY));
        cloudImages.add(new ComponentImage(cloud, WINDOW_WIDTH - 400, 20, Color.LIGHT_GRAY));
        cloudImages.add(new ComponentImage(cloud, WINDOW_WIDTH - 200, 80, Color.LIGHT_GRAY));

        firstCloud = cloudImages.get(0);
        secondCloud = cloudImages.get(1);
        thirdCloud = cloudImages.get(2);
    }

    @Override
    public void update() {
        firstCloud.x -= backgroundSpeed;
        secondCloud.x -= backgroundSpeed;
        thirdCloud.x -= backgroundSpeed;

        if (firstCloud.x <= -firstCloud.image.getWidth()) {
            firstCloud.x = WINDOW_WIDTH;
        }

        if (secondCloud.x <= -secondCloud.image.getWidth()) {
            secondCloud.x = WINDOW_WIDTH;
        }

        if (thirdCloud.x <= -thirdCloud.image.getWidth()) {
            thirdCloud.x = WINDOW_WIDTH;
        }
    }

    @Override
    public void draw(Graphics g) {
        switch (whichBackgroundColor){
            case DEFAULT:
                break;
            case DARK:
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
                break;
        }

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
        whichBackgroundColor = BackgroundColors.DEFAULT;
    }
}
