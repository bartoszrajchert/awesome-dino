package components.obstacles;

import components.dino.Dino;
import interfaces.Drawable;
import components.utility.Resource;

import java.awt.*;
import java.util.ArrayList;

import static main.GamePanel.*;

public class Obstacles implements Drawable {
    public static final int OBSTACLES_MIN_SPACE_BETWEEN = 250;
    public static final int OBSTACLES_MAX_SPACE_BETWEEN = 500;
    public static final int OBSTACLES_FIRST_OBSTACLE_X = 600;

    private final ArrayList<ObstacleImage> obstacleImages;
    private final int rangeSpaceBetweenObstacles;

    private final int maxIncomingObstacles;
    private ArrayList<ObstacleImage> incomingObstacles;

    public Obstacles() {
        obstacleImages = new ArrayList<>();

        // TODO: rewrite to support more obstacles which depend on window width
        maxIncomingObstacles = 5;

        ObstacleImage cactus1 = new ObstacleImage(new Resource().getResourceImage("/assets/Cactus-1.png"), 0);
        ObstacleImage cactus2 = new ObstacleImage(new Resource().getResourceImage("/assets/Cactus-2.png"), 0);
        ObstacleImage cactusTriple = new ObstacleImage(new Resource().getResourceImage("/assets/Cactus-3.png"), 0);
        ObstacleImage cactusDouble1 = new ObstacleImage(new Resource().getResourceImage("/assets/Cactus-4.png"), 0);
        ObstacleImage cactusDouble2 = new ObstacleImage(new Resource().getResourceImage("/assets/Cactus-5.png"), 0);

        rangeSpaceBetweenObstacles = OBSTACLES_MAX_SPACE_BETWEEN - OBSTACLES_MIN_SPACE_BETWEEN + 1;

        obstacleImages.add(cactus1);
        obstacleImages.add(cactus2);
        obstacleImages.add(cactusTriple);
        obstacleImages.add(cactusDouble1);
        obstacleImages.add(cactusDouble2);

        initFirstObstacles();
    }

    public ObstacleImage getRandomObstacle() {
        int randCactus = (int) (Math.random() * (obstacleImages.size()));
        ObstacleImage randObstacle = obstacleImages.get(randCactus);
        return new ObstacleImage(randObstacle.image, randObstacle.x);
    }

    public ObstacleImage getRandomObstacle(int x) {
        int randCactus = (int) (Math.random() * (obstacleImages.size()));
        ObstacleImage randObstacle = obstacleImages.get(randCactus);
        return new ObstacleImage(randObstacle.image, x);
    }

    public int getRandomSpace() {
        return (int) (Math.random() * rangeSpaceBetweenObstacles) + OBSTACLES_MIN_SPACE_BETWEEN;
    }

    public void initFirstObstacles() {
        incomingObstacles = new ArrayList<>();

        for (int i = 0; i < maxIncomingObstacles; i++) {
            ObstacleImage rand = getRandomObstacle();

            incomingObstacles.add(rand);
            if (i == 0) {
                incomingObstacles.get(0).x = OBSTACLES_FIRST_OBSTACLE_X;
                incomingObstacles.get(0).setSpaceBehind(getRandomSpace());
            } else {
                incomingObstacles.get(i).x = incomingObstacles.get(i - 1).x + incomingObstacles.get(i - 1).getSpaceBehind();
                incomingObstacles.get(i).setSpaceBehind(getRandomSpace());
            }
        }
    }

    public void reset() {
        initFirstObstacles();
    }

    public void update() {
        for (ObstacleImage obstacle : incomingObstacles) {
            obstacle.setX(obstacle.x - gameSpeed);
        }

        if (incomingObstacles.get(0).x < -incomingObstacles.get(0).image.getWidth()) {
            incomingObstacles.remove(0);
            incomingObstacles.add(getRandomObstacle(
                    incomingObstacles.get(incomingObstacles.size() - 1).x + incomingObstacles.get(incomingObstacles.size() - 1).getSpaceBehind()
            ));
            incomingObstacles.get(incomingObstacles.size() - 1).setSpaceBehind(getRandomSpace());
        }

        isCollision();
    }

    public boolean isCollision() {
        for (ObstacleImage obstacle : incomingObstacles) {
            if (Dino.getRectCollision().intersects(obstacle.getRectCollision())) {
                System.out.println("Collision");
                return true;
            }
        }
        return false;
    }

    public void draw(Graphics g) {
        for (ObstacleImage obstacle : incomingObstacles) {
            if (DEBUG_MODE) {
                g.setColor(obstacle.debugColor);
                g.drawRect(obstacle.x, obstacle.y, obstacle.image.getWidth(), obstacle.image.getHeight());
            }
            g.drawImage(obstacle.image, obstacle.x, obstacle.y, null);
        }
    }
}
