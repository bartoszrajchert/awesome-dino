package components.ui;

import components.utility.Resource;
import components.utility.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static components.dino.Dino.setMario;
import static main.GameWindow.WINDOW_HEIGHT;
import static main.GameWindow.WINDOW_WIDTH;

public class Intro {
    public static boolean IS_MARIO = false;

    private BufferedImage text = new Resource().getResourceImage("/assets/Intro.png");
    private ImageIcon icon = new ImageIcon(text);
    public JLabel label = new JLabel(icon);

    private final Sound overworld = new Sound("/assets/sounds/overworld.wav");

    public Intro() {
        label.setBounds((WINDOW_WIDTH - text.getWidth()) / 2, (WINDOW_HEIGHT - text.getHeight()) / 2 - 50, text.getWidth(), text.getHeight());
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                IS_MARIO = true;
                text = new Resource().getResourceImage("/assets/Intro-mario.png");
                icon = new ImageIcon(text);
                label.setIcon(icon);
                setMario();
                overworld.playInLoop();
            }
        });
    }

    public void setVisible(boolean val) {
        label.setVisible(val);
    }
}
