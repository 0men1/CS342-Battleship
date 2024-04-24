package gamebackend;

public class Ship {
    int size;
    boolean isVert;
    int health;

    public Ship(boolean isVert_, int size_) {
        isVert = isVert_;
        size = size_;
        health = size_;
    }

    public void hit() {
        health--;
    }
}
