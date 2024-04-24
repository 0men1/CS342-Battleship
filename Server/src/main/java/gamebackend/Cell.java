package gamebackend;

public class Cell {

    // not null is a ship
    // null is water
    Ship ship;

    public Cell(Ship ship_) {
        ship = ship_;
    }
}
