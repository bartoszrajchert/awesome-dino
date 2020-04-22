package components.ui;

import components.dino.Dino;
import components.utility.Resource;
import components.utility.Sound;
import interfaces.Drawable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static main.GameWindow.WINDOW_HEIGHT;
import static main.GameWindow.WINDOW_WIDTH;

public class Intro implements Drawable {
    public static boolean IS_MARIO = false;

    private BufferedImage text = new Resource().getResourceImage("/assets/Intro.png");
    public JLabel label = new JLabel();

    private final Sound overworld = new Sound("/assets/sounds/overworld.wav");

    public Intro() {
        label.setBounds((WINDOW_WIDTH - text.getWidth()) / 2, (WINDOW_HEIGHT - text.getHeight()) / 2 - 50, text.getWidth(), text.getHeight());
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                IS_MARIO = true;
                changeIntroTextToMario();
                Dino.setMario();
                overworld.playInLoop();
            }
        });
    }

    public void setVisible(boolean val) {
        label.setVisible(val);
    }

    public void changeIntroTextToMario() {text = new Resource().getResourceImage("/assets/Intro-mario.png");}

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(text, (WINDOW_WIDTH - text.getWidth())/ 2, (WINDOW_HEIGHT - text.getHeight()) / 2 - 50, null);
    }

    @Override
    public void reset() {

    }
}
