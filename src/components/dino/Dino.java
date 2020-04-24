package components.dino;

import components.utility.*;
import interfaces.Drawable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static components.ground.Ground.GROUND_Y;
import static main.GamePanel.DEBUG_MODE;
import static main.GamePanel.GAME_GRAVITY;

public class Dino implements Drawable {
    private static final int DINO_JUMP_STRENGTH = 12;
    private static final int DINO_FALL_STRENGTH = 8;
    private static final float DINO_START_X = 50;
    private static int DINO_RUNNING_ANIMATION_DELTA_TIME = 60;

    private static DinoStates dinoState;

    private static float x, y;
    private static float speedY;

    /**
     * This variable is for checking y before dino hit the ground.
     * Without it dino for msPerFrame ms is under the ground.
     */
    private static float TEMP_y;

    private static BufferedImage idleImage;
    private static BufferedImage jumpImage;
    private static Animation runAnimation;
    private static BufferedImage dieImage;

    public static ArrayList<Coordinates> constructedCoordinates;
    private static Coordinates collisionLeft;
    private static Coordinates collisionMiddle;
    private static Coordinates collisionRight;

    /**
     * It eliminates system delay between typed key event and pressed key event
     */
    public static boolean jumpRequested;

    private static Sound jumpSound;
    public static Sound gameOverSound;

    public Dino() {
        idleImage = new Resource().getResourceImage("/assets/Dino-stand.png");
        jumpImage = new Resource().getResourceImage("/assets/Dino-stand.png");;
        runAnimation = new Animation(DINO_RUNNING_ANIMATION_DELTA_TIME);
        runAnimation.addFrame(new Resource().getResourceImage("/assets/Dino-left-up.png"));
        runAnimation.addFrame(new Resource().getResourceImage("/assets/Dino-right-up.png"));
        dieImage = new Resource().getResourceImage("/assets/Dino-big-eyes.png");

        x = DINO_START_X;
        y = GROUND_Y - idleImage.getHeight();
        dinoState = DinoStates.IDLE;

        jumpSound = new Sound("/assets/sounds/button-press.wav");
        gameOverSound = new Sound("/assets/sounds/hit.wav");

        // Collision adjustments.
        // It is modified version from chromium source code.
        // ---------------------------------------------------
        //    ______
        //  _|      |-|
        // | | dino | |
        // |_|      |_|
        //   |_____ |
        int borderSize = 1;
        constructedCoordinates = new ArrayList<>();

        collisionLeft = new Coordinates(0, 15, 11 - borderSize, 21 - borderSize);
        collisionMiddle = new Coordinates(10, 0, 22 - borderSize, 45 - borderSize);
        collisionRight = new Coordinates(31, 0, 10 - borderSize, 21 - borderSize);

        constructedCoordinates.add(new Coordinates((int) x, (int) y + collisionLeft.y, collisionLeft.width, collisionLeft.height));
        constructedCoordinates.add(new Coordinates((int) x + collisionMiddle.x, (int) y, collisionMiddle.width, collisionMiddle.height));
        constructedCoordinates.add(new Coordinates((int) x + collisionRight.x, (int) y, collisionRight.width, collisionRight.height));
    }

    @Override
    public void draw(Graphics g) {
        if (DEBUG_MODE) {
            for (Coordinates coordinates : constructedCoordinates) {
                g.setColor(Color.BLACK);
                g.drawRect(coordinates.x, coordinates.y, coordinates.width, coordinates.height);
            }
        }
        switch (dinoState) {
            case IDLE:
                g.drawImage(idleImage, (int)x, (int)y, null);
                break;
            case JUMPING:
                g.drawImage(jumpImage, (int)x, (int)y, null);
                break;
            case RUNNING:
                runAnimation.update();
                g.drawImage(runAnimation.getFrame(), (int)x, (int)y, null);
                break;
            case DIE:
                g.drawImage(dieImage, (int)x, (int)y, null);
                break;
        }
    }

    /**
     *
     */
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
            constructedCoordinates.get(0).x = (int) x;
            constructedCoordinates.get(0).y = (int) y + collisionLeft.y;

            constructedCoordinates.get(1).x = (int) x + collisionMiddle.x;
            constructedCoordinates.get(1).y = (int) y;

            constructedCoordinates.get(2).x = (int) x + collisionRight.x;
            constructedCoordinates.get(2).y = (int) y;
        } else {
            constructedCoordinates.get(0).x = (int) x;
            constructedCoordinates.get(0).y = (int) y;
        }

    }

    @Override
    public void reset() {
        y = GROUND_Y - idleImage.getHeight();
        run();
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

    public static void setMario() {
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
        constructedCoordinates.add(new Coordinates((int) x, (int) y, idleImage.getWidth(), idleImage.getHeight()));
        System.out.println("MARIOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
    }
}
