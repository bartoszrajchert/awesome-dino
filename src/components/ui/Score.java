package components.ui;

import components.utility.Sound;
import components.utility.DeltaTime;
import interfaces.Drawable;

import java.awt.*;
import java.io.*;

import static main.GamePanel.GAME_MAX_SPEED;
import static main.GamePanel.gameSpeed;
import static main.GameWindow.WINDOW_WIDTH;

public class Score implements Drawable {
    private static final String SCORE_FILE_NAME = "highscore.txt";
    public static final int SCORE_MAX_ZEROS = 5;
    public static final int SCORE_DELTA_TIME = 100 / gameSpeed * 5;

    private int score;
    private int highScore;

    private final DeltaTime deltaTime;

    private final Sound scoreSound;
    private boolean isPlayed = false;
    private boolean isChanged = false;

    public Score() {
        score = 0;
        deltaTime = new DeltaTime(SCORE_DELTA_TIME);
        scoreSound = new Sound("/assets/sounds/score-reached.wav");

        highScore = readHighScore();
    }

    private String scoreBuilder(int score) {
        StringBuilder ret = new StringBuilder(Integer.toString(score));
        char zero = '0';

        for (int i = 0; i < SCORE_MAX_ZEROS - Integer.toString(score).length(); i++) {
            ret.insert(0, zero);
        }

        return ret.toString();
    }

    private void changeGameSpeed() {
        if (score%220 == 0 && !isChanged && gameSpeed < GAME_MAX_SPEED) {
            isChanged = true;
            gameSpeed += 1;
        }
    }

    private void playSound() {
        if (score > 0 && score%100 == 0 && !isPlayed) {
            isPlayed = true;
            scoreSound.play();
        }
    }

    private boolean isHighScore() {
        return highScore < score;
    }

    /**
     * https://stackoverflow.com/questions/12350248/java-difference-between-filewriter-and-bufferedwriter
     * -------------------------------------------------------------------------------------------------------
     * BufferedWriter is more efficient if you
     *  - have multiple writes between flush/close
     *  - ! the writes are small compared with the buffer size.
     * -------------------------------------------------------------------------------------------------------
     * BufferedWriter is more efficient. It saves up small writes and writes in one larger chunk if memory
     * serves me correctly. If you are doing lots of small writes then I would use BufferedWriter. Calling
     * write calls to the OS which is slow so having as few writes as possible is usually desirable.
     * -------------------------------------------------------------------------------------------------------
     */
    public void writeHighScore() {
        if (isHighScore()) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE_NAME));
                writer.write(Integer.toString(score));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            highScore = score;
            System.out.println("New high score");
        }
    }

    public boolean checkFileExistence() {
        File file = new File(SCORE_FILE_NAME);
        return file.exists();
    }

    public int readHighScore() {
        int highScore = 0;
        if (checkFileExistence()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE_NAME));
                highScore = Integer.parseInt(reader.readLine());
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("High score read");
        return highScore;
    }

    @Override
    public void update() {
        if (deltaTime.canExecute()) {
            isPlayed = false;
            isChanged = false;
            score += 1;
        }

        playSound();
        changeGameSpeed();
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.setFont(new Font("Consolas", Font.BOLD, 18));
        g.drawString(scoreBuilder(score), WINDOW_WIDTH - 100, 40);
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("HS " + scoreBuilder(highScore), WINDOW_WIDTH - 200, 40);
    }

    @Override
    public void reset() {
        score = 0;
    }
}
