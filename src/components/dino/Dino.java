package components.dino;

import components.utility.*;
import interfaces.Drawable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static components.ground.Ground.GROUND_Y;
import static main.GamePanel.debugMode;
import static main.GamePanel.GAME_GRAVITY;

public class Dino implements Drawable {
    private static final int DINO_JUMP_STRENGTH = 12;
    private static final int DINO_FALL_STRENGTH = 8;
    private static final float DINO_START_X = 50;
    private static int DINO_RUNNING_ANIMATION_DELTA_TIME = 60;
    private static final int DINO_BORDER_SIZE = 1;

    private static final float X = DINO_START_X;

    public static boolean isMario = false;
    public static boolean marioLoaded = false;

    private static DinoStates dinoState = DinoStates.IDLE;

    private static BufferedImage idleImage = new Resource().getResourceImage("/assets/Dino-stand.png");
    private static BufferedImage jumpImage = new Resource().getResourceImage("/assets/Dino-stand.png");
    private static Animation runAnimation = new Animation(DINO_RUNNING_ANIMATION_DELTA_TIME);
    private static BufferedImage dieImage = new Resource().getResourceImage("/assets/Dino-big-eyes.png");

    /**
     *  Collision adjustments.
     *  It is modified version from chromium source code.
     *  ---------------------------------------------------
     *     ______
     *   _|      |-|
     *  | | dino | |
     *  |_|      |_|
     *    |_____ |
     */
    public static ArrayList<Coordinates> constructedCoordinates = new ArrayList<>();
    private static final Coordinates collisionLeft = new Coordinates(0, 15, 11 - DINO_BORDER_SIZE, 21 - DINO_BORDER_SIZE);
    private static final Coordinates collisionMiddle = new Coordinates(10, 0, 22 - DINO_BORDER_SIZE, 45 - DINO_BORDER_SIZE);
    private static final Coordinates collisionRight = new Coordinates(31, 0, 10 - DINO_BORDER_SIZE, 21 - DINO_BORDER_SIZE);

    private static float y = GROUND_Y - idleImage.getHeight();
    private static float speedY;

    /**
     * This variable is for checking y before dino hit the ground.
     * Without it dino for msPerFrame ms is under the ground.
     */
    private static float TEMP_y;

    /**
     * It eliminates system delay between typed key event and pressed key event
     */
    public boolean jumpRequested;

    private static Sound jumpSound = new Sound("/assets/sounds/button-press.wav");
    public Sound gameOverSound = new Sound("/assets/sounds/hit.wav");

    public Dino() {
        runAnimation.addFrame(new Resource().getResourceImage("/assets/Dino-left-up.png"));
        runAnimation.addFrame(new Resource().getResourceImage("/assets/Dino-right-up.png"));

        constructedCoordinates.add(new Coordinates((int) X, (int) y + collisionLeft.y, collisionLeft.width, collisionLeft.height));
        constructedCoordinates.add(new Coordinates((int) X + collisionMiddle.x, (int) y, collisionMiddle.width, collisionMiddle.height));
        constructedCoordinates.add(new Coordinates((int) X + collisionRight.x, (int) y, collisionRight.width, collisionRight.height));
    }

    public void run() {
        dinoState = DinoStates.RUNNING;
    }

    public void jump() {
        if (dinoState == DinoStates.RUNNING) {
            dinoState = DinoStates.JUMPING;

            speedY = -DINO_JUMP_STRENGTH;
            y += speedY;

            // It prevents from layering sounds and game freeze
            if (!jumpSound.isNull()) {
                if (jumpSound.isOpen()) jumpSound.stop();
            }
            jumpSound.play();
        } else if (dinoState == DinoStates.JUMPING) {
            jumpRequested = true;
        }
    }

    public void fall() {
        if (dinoState == DinoStates.JUMPING) {
            speedY = DINO_FALL_STRENGTH;
            y += speedY;
        }
    }

    public void die() {
        dinoState = DinoStates.DIE;
        gameOverSound.play();
    }

    public void setMario() {
        System.out.println("\nMARIOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
        DINO_RUNNING_ANIMATION_DELTA_TIME = 100;

        idleImage = new Resource().getResourceImage("/assets/mario/Mario-welcome.png");
        jumpImage = new Resource().getResourceImage("/assets/mario/Mario-jump.png");
        runAnimation = new Animation(DINO_RUNNING_ANIMATION_DELTA_TIME);
        runAnimation.addFrame(new Resource().getResourceImage("/assets/mario/Mario-left-up.png"));
        runAnimation.addFrame(new Resource().getResourceImage("/assets/mario/Mario-right-up.png"));
        dieImage = new Resource().getResourceImage("/assets/mario/Mario-dead.png");

        jumpSound = new Sound("/assets/sounds/mario/jump.wav");
        gameOverSound = new Sound("/assets/sounds/mario/dead.wav");

        constructedCoordinates = new ArrayList<>();
        constructedCoordinates.add(new Coordinates((int) X, (int) y, idleImage.getWidth(), idleImage.getHeight()));

        marioLoaded = true;
        System.out.println("MARIOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
    }

    @Override
    public void draw(Graphics g) {
        if (debugMode) {
            for (Coordinates coordinates : constructedCoordinates) {
                g.setColor(Color.BLACK);
                g.drawRect(coordinates.x, coordinates.y, coordinates.width, coordinates.height);
            }
        }
        switch (dinoState) {
            case IDLE:
                g.drawImage(idleImage, (int) X, (int)y, null);
                break;
            case JUMPING:
                g.drawImage(jumpImage, (int) X, (int)y, null);
                break;
            case RUNNING:
                runAnimation.update();
                g.drawImage(runAnimation.getFrame(), (int) X, (int)y, null);
                break;
            case DIE:
                g.drawImage(dieImage, (int) X, (int)y, null);
                break;
        }
    }

    @Override
    public void update() {
        if((TEMP_y + speedY) >= GROUND_Y - idleImage.getHeight()) {
            speedY = 0;
            y = GROUND_Y - idleImage.getHeight();
            run();
            if (jumpRequested) {
                jump();
                jumpRequested = false;
            }
        } else if (dinoState == DinoStates.JUMPING){
            speedY += GAME_GRAVITY;
            y += speedY;
            TEMP_y = y;
        }
        if (constructedCoordinates.size() > 1) {
            constructedCoordinates.get(0).x = (int) X;
            constructedCoordinates.get(0).y = (int) y + collisionLeft.y;

            constructedCoordinates.get(1).x = (int) X + collisionMiddle.x;
            constructedCoordinates.get(1).y = (int) y;

            constructedCoordinates.get(2).x = (int) X + collisionRight.x;
            constructedCoordinates.get(2).y = (int) y;
        } else {
            constructedCoordinates.get(0).x = (int) X;
            constructedCoordinates.get(0).y = (int) y;
        }

    }

    @Override
    public void reset() {
        y = GROUND_Y - idleImage.getHeight();
        run();
    }
}
