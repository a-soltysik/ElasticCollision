package ElasticCollision;

public class Vector2D {

    private float x;
    private float y;

    public Vector2D(float x, float y) {
        setX(x);
        setY(y);
    }

    public Vector2D() {
        this(0, 0);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void set(float x, float y) {
        setX(x);
        setY(y);
    }

    public float getLength() {
        return (float) Math.sqrt(getX() * getX() + getY() * getY());
    }

    public float getDistance(Vector2D v2) {
        return (float) Math.sqrt(Math.pow(getX() - v2.getX(), 2) + Math.pow(getY() - v2.getY(), 2));
    }

    public Vector2D add(Vector2D v2) {
        Vector2D result = new Vector2D();
        result.set(getX() + v2.getX(), getY() + v2.getY());
        return result;
    }

    public Vector2D subtract(Vector2D v2) {
        Vector2D result = new Vector2D();
        result.set(getX() - v2.getX(), getY() - v2.getY());
        return result;
    }

    public Vector2D multiply(float scaleFactor) {
        Vector2D result = new Vector2D();
        result.setX(this.getX() * scaleFactor);
        result.setY(this.getY() * scaleFactor);
        return result;
    }

    public Vector2D divide(float scaleFactor) {
        Vector2D result = new Vector2D();
        result.setX(this.getX() / scaleFactor);
        result.setY(this.getY() / scaleFactor);
        return result;
    }

    public float dotProduct(Vector2D v2) {
        return getX() * v2.getX() + getY() * v2.getY();
    }

    public Vector2D normalize() {
        Vector2D temp = new Vector2D(getX(), getY());
        float len = temp.getLength();
        if (len != 0.0f) {
            temp.setX(temp.getX() / len);
            temp.setY(temp.getY() / len);
        } else {
            temp.setX(0.0f);
            temp.setY(0.0f);
        }
        return temp;
    }
    public String toString() {
        return "x: " + getX() + " y: " + getY();
    }


}