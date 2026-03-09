package Lib.ykVectorLib;

public abstract class Vector<T extends Vector<T>> {


    //addition de 2 vecteurs
    public abstract T add(T vector);
    //soustraction de 2 vecteurs
    public abstract T subtract(T vector);
    //multiplication par un scalaire
    public abstract T scale(double scalar);
    //produit scalaire de deux vecteurs
    public abstract double dot(T vector);
    //longueur du vecteur courant
    public abstract double getLength();
    //vecteur nul ?
    public boolean isZero() {
        return getLength() == 0;
    }
    //normalise un vecteur en vecteur unitaire
    @SuppressWarnings("unchecked") // hassoul le cast normalement c'est ok
    public T normalize() {
        if (isZero()) {
            throw new ArithmeticException("Impossible de normaliser un vecteur nul !");
        }
        scale(1.0 / getLength());
        return (T) this;
    }

    //TO STRING

    public abstract String toString();
}
