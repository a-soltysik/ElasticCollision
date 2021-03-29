package ElasticCollision;

import javax.swing.*;
import java.awt.*;

public class GameControl {

    GamePanel gamePanel = new GamePanel();

    public void run() {
        generateUI();
        startAnim();
    }

    private void generateUI() {
        JFrame gameFrame = new JFrame("Elastic Collisions");
        gamePanel = new GamePanel();
        gameFrame.getContentPane().add(BorderLayout.CENTER, gamePanel);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.pack();
        gameFrame.setVisible(true);
    }

    private void startAnim() {
        gamePanel.start();
    }
}