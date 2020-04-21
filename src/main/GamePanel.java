package main;

import components.background.Background;
import components.dino.Dino;
import components.ground.Ground;
import components.obstacles.Obstacles;
import components.ui.GameOver;
import components.ui.Intro;
import components.ui.Paused;
import components.ui.Score;
import components.utility.Resource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// TODO: https://stackoverflow.com/questions/43444193/use-addkeylistener-in-a-class-to-listen-keys-from-another-class
// TODO: w pewnych statach Dino trzeba blkować dostęp do innych klawisz (czyli żeby nic robiły)

public class GamePanel extends JPanel implements Runnable, KeyListener {
    public static boolean DEBUG_MODE = false;

    public static final int GAME_FPS = 60;
    public static final float GAME_GRAVITY = 0.64f;
    private static final int GAME_START_SPEED = 5;
    public static int GAME_MAX_SPEED = 15;

    public static int gameSpeed;

    private Thread mainThread;

    public boolean running = false;
    public boolean paused = false;              // TODO: https://www.geeksforgeeks.org/volatile-keyword-in-java/
    public boolean gameOver = false;
    public boolean intro = true;
    final Object pauseLock = new Object();

    Dino dino;
    Ground ground;
    Obstacles obstacles;
    Background background;

    Score score;
    GameOver gameOverUI;
    Paused pausedUI;
    Intro introUI;

    public GamePanel() {
        setSize(GameWindow.WINDOW_WIDTH, GameWindow.WINDOW_HEIGHT);

        gameSpeed = GAME_START_SPEED;

        dino = new Dino();
        ground = new Ground();
        obstacles = new Obstacles();
        background = new Background();

        score = new Score();
        gameOverUI = new GameOver();
        pausedUI = new Paused();
        introUI = new Intro();

        setLayout(null);
        add(introUI.label);

        mainThread = new Thread(this); //TODO why "this"
        mainThread.start();
        setVisible(true);
    }

    /*
     * Game status methods
     */
    public void startGame() {
        System.out.println("\n=====================GAME LOG=====================");
        System.out.println("Let the game begin\n");

        running = true;
        intro = false;
        introUI.setVisible(false);
    }

    public void resetGame() {
        gameOver = false;
        running = true;

        gameSpeed = GAME_START_SPEED;

        score.reset();

        dino.reset();
        obstacles.reset();
        ground.reset();
        background.reset();

        mainThread = new Thread(this); //TODO why "this"
        mainThread.start();
    }

    public void pauseGame() {
        paused = true;
        System.out.println("\nPaused");
    }

    public void resumeGame() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notify();
            System.out.println("\nResumed");
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        background.draw(g);

        if (paused) pausedUI.draw(g);
        if (gameOver) gameOverUI.draw(g);
        if (!intro) score.draw(g);

        ground.draw(g);
        dino.draw(g);
        obstacles.draw(g);
    }

    /*
     * Main game loop
     */
    // TODO:
    //  https://gamedev.stackexchange.com/questions/160329/java-game-loop-efficiency
    //  https://stackoverflow.com/questions/18283199/java-main-game-loop
    @Override
    public void run() {
        while (intro) {
            try {
                int msPerFrame = 1000 / GAME_FPS;
                Thread.sleep(msPerFrame);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
        }
        while(running) {
            // GAME TIMING
            try {
                int msPerFrame = 1000 / GAME_FPS;
                Thread.sleep(msPerFrame);

                if (paused) {
                    synchronized (pauseLock) {
                        repaint();
                        pauseLock.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // GAME LOGIC
            score.update();
            background.update();
            dino.update();
            ground.update();
            obstacles.update();

            if (obstacles.isCollision()) {
                dino.die();
                score.writeHighScore();
                gameOver = true;
                running = false;
            }

            // RENDER OUTPUT
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        /*
         *  Debug mode key: '`'
         */
        if (e.getKeyChar() == '`') {
            DEBUG_MODE = !DEBUG_MODE;
        }

        /*
         * Jump keys: ' ', 'w', 'VK_UP'
         * Jump key also starts the game and unpause
         */
        if(e.getKeyChar() == ' ' || e.getKeyChar() == 'w' || e.getKeyCode() == KeyEvent.VK_UP) {
            if (!paused && running) {
                dino.jump();
            } else if (paused && running) {
                resumeGame();
            }

            if (!running && !gameOver) {
                startGame();
                dino.run();
            } else if (gameOver) {
                resetGame();
            }
        }

        /*
         * Fall keys: 's', 'VK_DOWN'
         */
        if(e.getKeyChar() == 's' || e.getKeyCode() == KeyEvent.VK_DOWN) {
            if(!paused && running) {
                dino.fall();
//                dino.crouch();
            }
        }

        /*
         * Pause key: 'p', ESC
         */
        if(e.getKeyChar() == 'p' || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (!paused && running) {
                pauseGame();
            } else if (paused && running) {
                resumeGame();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // When pressing fast up and down key it will fly
//        if(e.getKeyChar() == 's' || e.getKeyCode() == KeyEvent.VK_DOWN) {
//            if(!paused && running) {
//                dino.run();
//            }
//        }
    }
}
