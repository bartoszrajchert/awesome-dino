package main;

import components.background.Background;
import components.dino.Dino;
import components.ground.Ground;
import components.obstacles.Obstacles;
import components.ui.GameOver;
import components.ui.Intro;
import components.ui.Paused;
import components.ui.Score;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static main.GameWindow.WINDOW_HEIGHT;
import static main.GameWindow.WINDOW_WIDTH;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    public static boolean DEBUG_MODE = false;

    public static final int GAME_FPS = 60;
    public static final float GAME_GRAVITY = 0.64f;
    private static final int GAME_START_SPEED = 5;
    public static int GAME_MAX_SPEED = 12;

    public static int gameSpeed;
    public static boolean isGameSpeedChanged = false;

    private Thread mainThread;

    public boolean running = false;
    public boolean paused = false;
    public boolean gameOver = false;
    public boolean intro = true;
    final Object pauseLock = new Object();

    Dino dino;
    Ground ground;
    Obstacles obstacles;
    Background background;

    Score scoreUI;
    GameOver gameOverUI;
    Paused pausedUI;
    Intro introUI;

    public GamePanel() {
        System.out.println("\nStartup log");
        System.out.println("-----------------------------------------------------");

        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        gameSpeed = GAME_START_SPEED;

        dino = new Dino();
        ground = new Ground();
        obstacles = new Obstacles();
        background = new Background();

        scoreUI = new Score();
        gameOverUI = new GameOver();
        pausedUI = new Paused();
        introUI = new Intro();

        setLayout(null);
        add(introUI.label);

        mainThread = new Thread(this); //TODO why "this"
        mainThread.start();
        setVisible(true);
    }

    public void startGame() {
        System.out.println("\nGame log");
        System.out.println("-----------------------------------------------------");

        running = true;
        intro = false;
        introUI.setVisible(false);
    }

    public void resetGame() {
        gameOver = false;
        running = true;

        gameSpeed = GAME_START_SPEED;

        scoreUI.reset();

        dino.reset();
        obstacles.reset();
        ground.reset();
        background.reset();

        if (introUI.IS_MARIO) {
            introUI.overworld.playInLoop();

            // It prevents from layering sounds
            if (dino.gameOverSound.isOpen()) dino.gameOverSound.stop();
        }

        mainThread = new Thread(this); //TODO why "this"
        mainThread.start();
    }

    public void pauseGame() {
        paused = true;
        System.out.println("Paused");
    }

    public void resumeGame() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notify();
            System.out.println("Resumed");
        }
    }

    private void changeGameSpeed() {
        if (scoreUI.score > 0 && scoreUI.score%260 == 0 && !isGameSpeedChanged && gameSpeed < GAME_MAX_SPEED) {
            isGameSpeedChanged = true;
            gameSpeed += 1;
        }
    }

    /**
     * MAIN PAINT METHOD
     * --------------------------------------------------------
     * @param g     Graphics
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        background.draw(g);

        if (paused) pausedUI.draw(g);
        if (gameOver) gameOverUI.draw(g);
        if (!intro) scoreUI.draw(g);

        ground.draw(g);
        dino.draw(g);
        obstacles.draw(g);

        if (intro) introUI.draw(g);
    }

    /**
     * MAIN GAME LOOP
     *
     * I'm aware that Thread.sleep() is not a good practice but it is
     * good enough for this game.
     *
     * ------------------------------------------------------------------------
     * Good resources:
     *  - https://gamedev.stackexchange.com/questions/160329/java-game-loop-efficiency
     *  - https://stackoverflow.com/questions/18283199/java-main-game-loop
     */
    @Override
    public void run() {
        // INTRO LOOP FOR EASTER EGG
        while (intro) {
            try {
                int msPerFrame = 1000 / GAME_FPS;
                Thread.sleep(msPerFrame);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            repaint();
        }

        // MAIN GAME LOOP
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
            changeGameSpeed();

            scoreUI.update();
            background.update();
            dino.update();
            ground.update();
            obstacles.update();

            if (obstacles.isCollision()) {
                dino.die();
                if (introUI.IS_MARIO) introUI.overworld.stop();
                scoreUI.writeHighScore();
                gameOver = true;
                running = false;
                System.out.println("Game over");
            }

            // RENDER OUTPUT
            repaint();
        }
    }

    /**
     * KEY BINDINGS
     *
     * -------------------------------------------
     * Debug mode: '`'
     * Jump: ' ', 'w', 'ARROW UP'
     * Fall: 's', 'ARROW DOWN'
     * Pause: 'p', 'ESC'
     * -------------------------------------------
     * @param e         KeyEvent
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // DEBUG
        if (e.getKeyChar() == '`') {
            DEBUG_MODE = !DEBUG_MODE;
        }

        // JUMP
        if(e.getKeyChar() == ' ' || e.getKeyChar() == 'w' || e.getKeyCode() == KeyEvent.VK_UP) {
            if (!paused && running) {
                dino.jump();
            } else if (paused && running) {
                resumeGame();
            }

            if (!running && !gameOver) {
                startGame();
                dino.run();
                dino.jump();
            } else if (gameOver) {
                resetGame();
            }
        }

        // FALL
        if(e.getKeyChar() == 's' || e.getKeyCode() == KeyEvent.VK_DOWN) {
            if(!paused && running) {
                dino.fall();
            }
        }

        // PAUSE
        if(e.getKeyChar() == 'p' || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (!paused && running) {
                pauseGame();
            } else if (paused && running) {
                resumeGame();
            }
        }
    }

    /**
     * Just checking if someone change mind to jump
     * right after hitting ground
     * --------------------------------------------------------
     * @param e     KeyEvent
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == ' ' || e.getKeyChar() == 'w' || e.getKeyCode() == KeyEvent.VK_UP)
            Dino.jumpRequested = false;
    }

    @Override
    public void keyTyped(KeyEvent e) { }
}
