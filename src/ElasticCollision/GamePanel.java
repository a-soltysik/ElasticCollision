package ElasticCollision;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.concurrent.CopyOnWriteArrayList;

public class GamePanel extends Canvas {

    Dimension size = new Dimension(800, 800);
    private CopyOnWriteArrayList<Ball> balls;
    private BufferStrategy strategy;
    private Graphics2D g2;
    private int currentFrameRate;
    private int ballCounter = 0;
    boolean enabled = false;

    public GamePanel() {
        setFocusable(true);
        requestFocus();
        setPreferredSize(size);
        setIgnoreRepaint(true);
        addMouseListener(new MouseListener());
    }

    private class MouseListener implements java.awt.event.MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            float radius = (float) Math.random() * 50;
            float rx = (float) e.getX();
            float ry = (float) e.getY();
            float vx = (float) Math.random() * 1000;
            float vy = (float) Math.random() * 1000;
            balls.add(new Ball(radius, radius, new Vector2D(rx, ry)));
            balls.get(ballCounter).setVelocity(vx, vy);
            ballCounter++;
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public void start() {
        enabled = true;
        balls = new CopyOnWriteArrayList<>();
        for (int i=0; i<10; i++) {
            float radius = (float) Math.random() * 30 + 20;
            float rx = (float) Math.random() * (getWidth() - radius);
            float ry = (float) Math.random() * (getHeight() - radius);
            float vx = (float) Math.random() * 1000;
            float vy = (float) Math.random() * 1000;
            balls.add(new Ball(radius, radius, new Vector2D(rx, ry)));
            balls.get(i).setVelocity(vx, vy);
            ballCounter++;
        }
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() {
                mainLoop();
                return null;
            }
        }.execute();
    }

    private void mainLoop() {
        long previousTime = System.currentTimeMillis();
        long currentTime, elapsedTime, totalElapsedTime = 0;
        int frameCount = 0;
        while (enabled) {
            currentTime = System.currentTimeMillis();
            elapsedTime = (currentTime - previousTime);
            totalElapsedTime += elapsedTime;
            if (totalElapsedTime > 1000) {
                currentFrameRate = frameCount;
                frameCount = 0;
                totalElapsedTime = 0;
            }
            update(elapsedTime / 1000f);
            render();
            previousTime = currentTime;
            frameCount++;
        }
    }

    public void update(float deltaTime) {
        moveBalls(deltaTime);
        checkCollisions();
    }

    private void render() {
        prepare();
        drawBackground();
        drawBalls();
        drawInfo();
        if (!strategy.contentsLost())
            strategy.show();
    }

    private void prepare() {
        if (strategy == null || strategy.contentsLost()) {
            createBufferStrategy(2);
            strategy = getBufferStrategy();
            Graphics g = strategy.getDrawGraphics();
            g2 = (Graphics2D) g;
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void drawBackground() {
        g2.setColor(Color.decode("#3e424b"));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
    }

    private void drawBalls() {
        for (Ball ball : balls) {
            ball.draw(g2);
        }
    }

    private void drawInfo() {
        int momentum = getMomentum();
        g2.setColor(Color.RED);
        g2.setFont(new Font("Cascadia Code", Font.BOLD, 15));
        g2.drawString("FPS: " + currentFrameRate, 15, 15);
        g2.drawString("Momentum: " + momentum, 15, 40);
        g2.drawString("Number of balls: " + ballCounter, 15, 65);
    }

    private int getMomentum() {
        float momentum = 0f;
        for (Ball ball : balls) {
            momentum += ball.getMass() * ball.getVelocity().getLength();
        }
        return Math.round(momentum);
    }

    private void checkCollisions() {
        for (Ball ball1 : balls) {
            checkWalls(ball1);
            for (Ball ball2 : balls) {
                if (ball2.equals(ball1))
                    continue;
                if (ball1.getPosition().getDistance(ball2.getPosition()) < ball1.getRadius() + ball2.getRadius())
                    conductCollision(ball1, ball2);
            }
        }
    }

    private void conductCollision(Ball ball1, Ball ball2) {
        Vector2D v1 = ball1.getVelocity();
        Vector2D v2 = ball2.getVelocity();
        Vector2D r1 = ball1.getPosition();
        Vector2D r2 = ball2.getPosition();
        Vector2D tempVec;
        float delta = (ball1.getRadius() + ball2.getRadius() - r1.getDistance(r2));
        if (ball1.getVelocity().getLength() > ball2.getVelocity().getLength()) {
            ball1.setPosition(ball1.getPosition().subtract(ball1.getVelocity().normalize().multiply(delta)));
            ball2.setPosition(ball2.getPosition().add(ball1.getVelocity().normalize().multiply(delta)));
        } else {
            ball1.setPosition(ball1.getPosition().add(ball2.getVelocity().normalize().multiply(delta)));
            ball2.setPosition(ball2.getPosition().subtract(ball2.getVelocity().normalize().multiply(delta)));
        }

        tempVec = (r1.subtract(r2))
                .multiply((float) ((v1.subtract(v2)).dotProduct(r1.subtract(r2)) / (Math.pow((r1.subtract(r2)).getLength(), 2))))
                .multiply(2 * ball2.getMass() / (ball1.getMass() + ball2.getMass()));
        ball1.setVelocity(v1.subtract(tempVec));

        tempVec = (r2.subtract(r1))
                .multiply((float) ((v2.subtract(v1)).dotProduct(r2.subtract(r1)) / (Math.pow((r2.subtract(r1)).getLength(), 2))))
                .multiply(2 * ball1.getMass() / (ball1.getMass() + ball2.getMass()));
        ball2.setVelocity(v2.subtract(tempVec));
    }

    public void checkWalls(Ball ball) {
        float delta;
        if ((delta = ball.getPosition().getX() - ball.getRadius()) < 0) {
            ball.setPosition(ball.getPosition().getX() - delta, ball.getPosition().getY());
            ball.setVelocity(-1f * ball.getVelocity().getX(), ball.getVelocity().getY());
        } else if ((delta = getWidth() - ball.getPosition().getX() - ball.getRadius()) < 0) {
            ball.setPosition(ball.getPosition().getX() + delta, ball.getPosition().getY());
            ball.setVelocity(-1f * ball.getVelocity().getX(), ball.getVelocity().getY());
        }
        if ((delta = ball.getPosition().getY() - ball.getRadius()) < 0) {
            ball.setPosition(ball.getPosition().getX(), ball.getPosition().getY() - delta);
            ball.setVelocity(ball.getVelocity().getX(), -1f * ball.getVelocity().getY());
        } else if ((delta = getHeight() - ball.getPosition().getY() - ball.getRadius()) < 0) {
            ball.setPosition(ball.getPosition().getX(), ball.getPosition().getY() + delta);
            ball.setVelocity(ball.getVelocity().getX(), -1f * ball.getVelocity().getY());
        }
    }

    private void moveBalls(float deltaTime) {
        for (Ball ball : balls) {
            ball.setPosition(
                    ball.getPosition().getX() + ball.getVelocity().getX() * deltaTime,
                    ball.getPosition().getY() + ball.getVelocity().getY() * deltaTime
            );
        }
    }
}