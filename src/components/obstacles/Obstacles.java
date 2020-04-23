package components.obstacles;

import components.dino.Dino;
import components.utility.Coordinates;
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
        return new ObstacleImage(randObstacle.getImage(), randObstacle.getX());
    }

    public ObstacleImage getRandomObstacle(int x) {
        int randCactus = (int) (Math.random() * (obstacleImages.size()));
        ObstacleImage randObstacle = obstacleImages.get(randCactus);
        return new ObstacleImage(randObstacle.getImage(), x);
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
                incomingObstacles.get(0).setX(OBSTACLES_FIRST_OBSTACLE_X);
                incomingObstacles.get(0).setSpaceBehind(getRandomSpace());
            } else {
                incomingObstacles.get(i).setX(incomingObstacles.get(i - 1).getX() + incomingObstacles.get(i - 1).getSpaceBehind());
                incomingObstacles.get(i).setSpaceBehind(getRandomSpace());
            }
        }
    }

    public void reset() {
        initFirstObstacles();
    }

    public void update() {
        for (ObstacleImage obstacle : incomingObstacles) {
            obstacle.setX(obstacle.getX() - gameSpeed);
        }

        if (incomingObstacles.get(0).getX() < -incomingObstacles.get(0).getImage().getWidth()) {
            incomingObstacles.remove(0);
            incomingObstacles.add(getRandomObstacle(
                    incomingObstacles.get(incomingObstacles.size() - 1).getX() + incomingObstacles.get(incomingObstacles.size() - 1).getSpaceBehind()
            ));
            incomingObstacles.get(incomingObstacles.size() - 1).setSpaceBehind(getRandomSpace());
        }

        isCollision();
    }

    public boolean isCollision() {
        for (ObstacleImage obstacle : incomingObstacles) {
            for (Coordinates dinoCoordinates : Dino.constructedCoordinates)
            if (dinoCoordinates.intersects(obstacle.coordinates)) {
                return true;
            }
        }
        return false;
    }

    public void draw(Graphics g) {
        for (ObstacleImage obstacle : incomingObstacles) {
            if (DEBUG_MODE) {
                g.setColor(obstacle.getDebugColor());
                g.drawRect(obstacle.coordinates.x, obstacle.coordinates.y, obstacle.coordinates.width, obstacle.coordinates.height);
            }
            g.drawImage(obstacle.getImage(), obstacle.getX(), obstacle.getY(), null);
        }
    }
}
