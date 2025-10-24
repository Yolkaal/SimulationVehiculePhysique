package Lib.ykVectorLib;

public class Vector2D extends Vector<Vector2D> {

    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D vector2D) {
        this.x = vector2D.getX();
        this.y = vector2D.getY();
    }

    @Override
    public Vector2D add(Vector2D vector) {
        this.x += vector.getX();
        this.y += vector.getY();
        return this;
    }

    @Override
    public Vector2D subtract(Vector2D vector) {
        this.x -= vector.getX();
        this.y -= vector.getY();
        return this;
    }

    @Override
    public Vector2D scale(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    @Override
    public double dot(Vector2D vector) {
        return this.x * vector.getX() + this.y * vector.getY();
    }

    @Override
    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }


    //GETTERS & SETTERS

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    //TO STRING

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
