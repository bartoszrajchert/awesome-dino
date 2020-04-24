package components.obstacles;

import components.dino.Dino;
import components.utility.Coordinates;
import interfaces.Drawable;
import components.utility.Resource;
import main.GamePanel;

import java.awt.*;
import java.util.ArrayList;

public class Obstacles implements Drawable {
    private static final int OBSTACLES_MIN_SPACE_BETWEEN = 250;
    private static final int OBSTACLES_MAX_SPACE_BETWEEN = 500;
    private static final int OBSTACLES_FIRST_OBSTACLE_X = 600;
    private static final int RANGE_SPACE_BETWEEN_OBSTACLES = OBSTACLES_MAX_SPACE_BETWEEN - OBSTACLES_MIN_SPACE_BETWEEN + 1;

    private static final ArrayList<ObstacleImage> OBSTACLE_IMAGES = new ArrayList<>();

    private static final int MAX_INCOMING_OBSTACLES = 5;
    private ArrayList<ObstacleImage> incomingObstacles;

    public Obstacles() {
        ObstacleImage cactus1 = new ObstacleImage(new Resource().getResourceImage("/assets/Cactus-1.png"));
        ObstacleImage cactus2 = new ObstacleImage(new Resource().getResourceImage("/assets/Cactus-2.png"));
        ObstacleImage cactusTriple = new ObstacleImage(new Resource().getResourceImage("/assets/Cactus-3.png"));
        ObstacleImage cactusDouble1 = new ObstacleImage(new Resource().getResourceImage("/assets/Cactus-4.png"));
        ObstacleImage cactusDouble2 = new ObstacleImage(new Resource().getResourceImage("/assets/Cactus-5.png"));

        OBSTACLE_IMAGES.add(cactus1);
        OBSTACLE_IMAGES.add(cactus2);
        OBSTACLE_IMAGES.add(cactusTriple);
        OBSTACLE_IMAGES.add(cactusDouble1);
        OBSTACLE_IMAGES.add(cactusDouble2);

        initFirstObstacles();
    }

    private void initFirstObstacles() {
        incomingObstacles = new ArrayList<>();

        for (int i = 0; i < MAX_INCOMING_OBSTACLES; i++) {
            ObstacleImage rand = getRandomObstacle();

            incomingObstacles.add(rand);
            if (i == 0) {
                incomingObstacles.get(0).setX(OBSTACLES_FIRST_OBSTACLE_X);
            } else {
                incomingObstacles.get(i).setX(incomingObstacles.get(i - 1).getX() + incomingObstacles.get(i - 1).getSpaceBehind());
            }
        }
    }

    private ObstacleImage getRandomObstacle() {
        int randCactus = (int) (Math.random() * (OBSTACLE_IMAGES.size()));
        ObstacleImage randObstacle = OBSTACLE_IMAGES.get(randCactus);

        return new ObstacleImage(randObstacle.getOBSTACLE_IMAGE(), getRandomSpace());
    }

    private ObstacleImage getRandomObstacle(int x) {
        int randCactus = (int) (Math.random() * (OBSTACLE_IMAGES.size()));
        ObstacleImage randObstacle = OBSTACLE_IMAGES.get(randCactus);

        return new ObstacleImage(randObstacle.getOBSTACLE_IMAGE(), x, getRandomSpace());
    }

    private int getRandomSpace() {
        return (int) (Math.random() * RANGE_SPACE_BETWEEN_OBSTACLES) + OBSTACLES_MIN_SPACE_BETWEEN;
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

    @Override
    public void reset() {
        initFirstObstacles();
    }

    @Override
    public void update() {
        for (ObstacleImage obstacle : incomingObstacles) {
            obstacle.setX(obstacle.getX() - GamePanel.gameSpeed);
        }

        if (incomingObstacles.get(0).getX() < -incomingObstacles.get(0).getOBSTACLE_IMAGE().getWidth()) {
            ObstacleImage lastIncomingObstacle = incomingObstacles.get(incomingObstacles.size() - 1);

            incomingObstacles.remove(0);
            incomingObstacles.add(getRandomObstacle(lastIncomingObstacle.getX() + lastIncomingObstacle.getSpaceBehind()));
            incomingObstacles.get(incomingObstacles.size() - 1).setSpaceBehind(getRandomSpace());
        }
    }

    @Override
    public void draw(Graphics g) {
        for (ObstacleImage obstacle : incomingObstacles) {
            if (GamePanel.debugMode) {
                g.setColor(obstacle.getDebugColor());
                g.drawRect(obstacle.coordinates.x, obstacle.coordinates.y, obstacle.coordinates.width, obstacle.coordinates.height);
            }
            g.drawImage(obstacle.getOBSTACLE_IMAGE(), obstacle.getX(), obstacle.getY(), null);
        }
    }
}
