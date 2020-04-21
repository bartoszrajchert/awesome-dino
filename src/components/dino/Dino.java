package components.dino;

import interfaces.Drawable;
import components.utility.Sound;
import components.utility.Animation;
import components.utility.Resource;

import java.awt.*;
import java.awt.image.BufferedImage;

import static components.ground.Ground.GROUND_Y;
import static main.GamePanel.DEBUG_MODE;
import static main.GamePanel.GAME_GRAVITY;

public class Dino implements Drawable {
    private static final int DINO_JUMP_STRENGTH = 12;
    private static final int DINO_FALL_STRENGTH = 8;
    private static final float DINO_START_X = 50;
    private static int DINO_RUNNING_ANIMATION_DELTA_TIME = 60;

    private DinoStates dinoState;

    private float x, y;
    private float TEMP_y;
    private float speedY;

    private static BufferedImage idleImage;
    private static BufferedImage jumpImage;
    private static Animation runAnimation;
    private static Animation crouchAnimation;
    private static BufferedImage dieImage;

    private static Rectangle rectCollision;

    private Sound jumpSound;

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

        rectCollision = new Rectangle();
        rectCollision.x = (int) x;
        rectCollision.y = (int) y;

        jumpSound = new Sound("/assets/sounds/button-press.wav");
    }

    @Override
    public void draw(Graphics g) {
        switch (dinoState) {
            case IDLE:
                if (DEBUG_MODE) {
                    g.setColor(Color.BLACK);
                    g.drawRect((int) x, (int) y, idleImage.getWidth(), idleImage.getHeight());
                }
                g.drawImage(idleImage, (int)x, (int)y, null);
                break;
            case JUMPING:
                if (DEBUG_MODE) {
                    g.setColor(Color.BLACK);
                    g.drawRect((int) x, (int) y, jumpImage.getWidth(), jumpImage.getHeight());
                }
                g.drawImage(jumpImage, (int)x, (int)y, null);
                break;
            case CROUCHING:
                crouchAnimation.update();
                if (DEBUG_MODE) {
                    g.setColor(Color.BLACK);
                    g.drawRect((int) x, (int) y + idleImage.getHeight() - crouchAnimation.getFrame().getHeight(), crouchAnimation.getFrame().getWidth(), crouchAnimation.getFrame().getHeight());
                }
                g.drawImage(crouchAnimation.getFrame(), (int)x, (int)y + idleImage.getHeight() - crouchAnimation.getFrame().getHeight(), null);
                break;
            case RUNNING:
                runAnimation.update();
                if (DEBUG_MODE) {
                    g.setColor(Color.BLACK);
                    g.drawRect((int) x, (int) y, runAnimation.getFrame().getWidth(), runAnimation.getFrame().getHeight());
                }
                g.drawImage(runAnimation.getFrame(), (int)x, (int)y, null);
                break;
            case DIE:
                if (DEBUG_MODE) {
                    g.setColor(Color.BLACK);
                    g.drawRect((int) x, (int) y, idleImage.getWidth(), idleImage.getHeight());
                }
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

        rectCollision.x = (int) x;
        rectCollision.y = (int) y;
        if (dinoState != DinoStates.CROUCHING) {
            rectCollision.height = idleImage.getHeight();
            rectCollision.width = idleImage.getWidth();
        }
    }

    @Override
    public void reset() {
        y = GROUND_Y - idleImage.getHeight();
        run();
    }

    public static Rectangle getRectCollision() {
        return rectCollision;
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

//    public void crouch() {
//        if (dinoState != DinoStates.JUMPING) {
//            dinoState = DinoStates.CROUCHING;
//            crouchAnimation.update();
//        }
//    }

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
    }
}
