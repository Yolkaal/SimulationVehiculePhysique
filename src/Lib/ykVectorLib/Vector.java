package Lib.ykVectorLib;

public abstract class Vector<T extends Vector<T>> {

    public abstract T add(T vector);
    public abstract T subtract(T vector);
    public abstract T scale(double scalar);
    public abstract double dot(T vector);
    public abstract double getLength();

    public boolean isZero() {
        return getLength() == 0;
    }

    public T normalize() {
        if (isZero()) {
            throw new ArithmeticException("Impossible de normaliser un vecteur nul !");
        }
        return scale(1.0 / getLength());
    }
}
