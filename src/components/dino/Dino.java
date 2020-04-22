package components.dino;

import components.utility.CollisionBox;
import interfaces.Drawable;
import components.utility.Sound;
import components.utility.Animation;
import components.utility.Resource;

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
    private static float TEMP_y;
    private static float speedY;

    private static BufferedImage idleImage;
    private static BufferedImage jumpImage;
    private static Animation runAnimation;
    private static Animation crouchAnimation;
    private static BufferedImage dieImage;

    public static ArrayList<CollisionBox> constructedCollisionBox;

    private static Sound jumpSound;

    public Dino() {
        idleImage = new Resource().getResourceImage("/assets/Dino-stand.png");
        jumpImage = new Resource().getResourceImage("/assets/Dino-stand.png");;
        runAnimation = new Animation(DINO_RUNNING_ANIMATION_DELTA_TIME);
        runAnimation.addFrame(new Resource().getResourceImage("/assets/Dino-left-up.png"));
        runAnimation.addFrame(new Resource().getResourceImage("/assets/Dino-right-up.png"));
        crouchAnimation = new Animation(DINO_RUNNING_ANIMATION_DELTA_TIME);
        crouchAnimation.addFrame(new Resource().getResourceImage("/assets/Dino-below-left-up.png"));
        crouchAnimation.addFrame(new Resource().getResourceImage("/assets/Dino-below-right-up.png"));
        dieImage = new Resource().getResourceImage("/assets/Dino-big-eyes.png");

        x = DINO_START_X;
        y = GROUND_Y - idleImage.getHeight();
        dinoState = DinoStates.IDLE;

        jumpSound = new Sound("/assets/sounds/button-press.wav");

        // Collision adjustments.
        // It is modified version from chromium source code.
        // ---------------------------------------------------
        //    ______
        //  _|      |-|
        // | |<---->| |
        // |_| dino |_|
        //   |_____ |
        int borderSize = 1;
        constructedCollisionBox = new ArrayList<>();
        constructedCollisionBox.add(new CollisionBox((int) x, (int) y + 15, 11 - borderSize, 21 - borderSize));
        constructedCollisionBox.add(new CollisionBox((int) x + 10, (int) y, 22 - borderSize, 45 - borderSize));
        constructedCollisionBox.add(new CollisionBox((int) x + 31, (int) y, 10 - borderSize, 21 - borderSize));
    }

    @Override
    public void draw(Graphics g) {
        if (DEBUG_MODE) {
            for (CollisionBox collisionBox : constructedCollisionBox) {
                g.setColor(Color.BLACK);
                g.drawRect(collisionBox.x, collisionBox.y, collisionBox.width, collisionBox.height);
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

    @Override
    public void update() {
        if((TEMP_y + speedY) >= GROUND_Y - idleImage.getHeight()) {
            speedY = 0;
            y = GROUND_Y - idleImage.getHeight();
            run();
        } else if (dinoState == DinoStates.JUMPING){
            speedY += GAME_GRAVITY;
            y += speedY;
            TEMP_y = y;
        }
        if (constructedCollisionBox.size() > 1) {
            constructedCollisionBox.get(0).x = (int) x;
            constructedCollisionBox.get(0).y = (int) y + 15;

            constructedCollisionBox.get(1).x = (int) x + 10;
            constructedCollisionBox.get(1).y = (int) y;

            constructedCollisionBox.get(2).x = (int) x + 31;
            constructedCollisionBox.get(2).y = (int) y;
        } else {
            constructedCollisionBox.get(0).x = (int) x;
            constructedCollisionBox.get(0).y = (int) y;
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

            jumpSound.play();
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
    }

    public static void setMario() {
        DINO_RUNNING_ANIMATION_DELTA_TIME = 100;

        idleImage = new Resource().getResourceImage("/assets/mario/Mario-welcome.png");
        jumpImage = new Resource().getResourceImage("/assets/mario/Mario-jump.png");
        runAnimation = new Animation(DINO_RUNNING_ANIMATION_DELTA_TIME);
        runAnimation.addFrame(new Resource().getResourceImage("/assets/mario/Mario-left-up.png"));
        runAnimation.addFrame(new Resource().getResourceImage("/assets/mario/Mario-right-up.png"));
        dieImage = new Resource().getResourceImage("/assets/mario/Mario-dead.png");

        constructedCollisionBox = new ArrayList<>();
        constructedCollisionBox.add(new CollisionBox((int) x, (int) y, idleImage.getWidth(), idleImage.getHeight()));
    }
}
