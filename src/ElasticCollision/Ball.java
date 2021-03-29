package ElasticCollision;

import java.awt.*;

public class Ball {

    private Vector2D position;
    private Vector2D velocity;
    private float radius;
    private float mass;
    private Color color;

    public Ball() {
        this(
                50f,
                1f,
                new Vector2D(0, 0),
                randomColor()
        );
    }

    public Ball(float radius, float mass, Vector2D position) {
        this(
                radius,
                mass,
                position,
                randomColor()
        );
    }

    public Ball(float radius, float mass, Vector2D position, Color color) {
        this.radius = radius;
        this.position = position;
        this.mass = mass;
        this.color = color;
        velocity = new Vector2D();
    }

    public static Color randomColor() {
        return new Color(
                (int) (Math.random() * 256),
                (int) (Math.random() * 256),
                (int) (Math.random() * 256)
        );
    }

    public Color getColor() {
        return color;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.fillOval((int) (getPosition().getX() - getRadius()), (int) (getPosition().getY() - getRadius()), (int) (2 * getRadius()), (int) (2 * getRadius()));
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Cascadia Code", Font.PLAIN, 15));
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void setPosition(float x, float y) {
        getPosition().setX(x);
        getPosition().setY(y);
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public void setVelocity(float x, float y) {
        getVelocity().setX(x);
        getVelocity().setY(y);
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

}
