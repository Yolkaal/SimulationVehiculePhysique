package Lib.ykVectorLib;

public class Vector3D extends Vector<Vector3D>{

    private double x;
    private double y;
    private double z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D(Vector3D vector3D) {
        this.x = vector3D.getX();
        this.y = vector3D.getY();
        this.z = vector3D.getZ();
    }

    @Override
    public Vector3D add(Vector3D vector) {
        this.x += vector.getX();
        this.y += vector.getY();
        this.z += vector.getZ();
        return this;
    }

    @Override
    public Vector3D subtract(Vector3D vector) {
        this.x -= vector.getX();
        this.y -= vector.getY();
        this.z -= vector.getZ();
        return this;
    }

    @Override
    public Vector3D scale(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }

    @Override
    public double dot(Vector3D vector) {
        return this.x * vector.x + this.y * vector.y + this.z * vector.z;
    }

    @Override
    public double getLength() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    //GETTERS & SETTERS

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    //TO STRING

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
