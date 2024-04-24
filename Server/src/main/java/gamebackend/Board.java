package gamebackend;
import java.util.ArrayList;

public class Board {
    Cell[][] board = new Cell[10][10];
    ArrayList<Ship> ships = new ArrayList<>();

    public void initializeBoard() {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                board[x][y] = new Cell(null);
            }
        }
    }


    public boolean shoot(int x, int y) {
        if (x > 10 || x < 0) {
            return false;
        } else if (y > 10 || y < 0) {
            return false;
        }

        if (board[x][y] != null) {
            board[x][y].ship.hit();
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Ship> checkForDestroyedShips() {
        ArrayList<Ship> toRet = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (ships.get(i).health == 0) {
                toRet.add(ships.get(i));
            }
        }
        return toRet;
    }


    public boolean placeShip(Ship ship, int x, int y) {
        if (x > 10 || x < 0) {
            return false;
        } else if (y > 10 || y < 0) {
            return false;
        } else {
            if (canPlaceShip(ship, x, y)) {
                if (ship.isVert) {
                    for (int i = y; i < (ship.size + y); i++) {
                        board[x][i].ship = ship;
                    }
                } else {
                    for (int i = x; i < (ship.size + x); i++) {
                        board[i][y].ship = ship;
                    }
                }
                ships.add(ship);
                return true;
            }
        }
        return false;
    }


    public boolean canPlaceShip(Ship ship, int x, int y) {
        if (ship.isVert) {
            if ((ship.size + y) - 1 < 10) {
                for (int i = y; i < (ship.size + y); i++) {
                    if (board[x][i].ship != null) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } else {
            if ((ship.size + x) - 1 < 10) {
                for (int i = x; i < (ship.size + x); i++) {
                    if (board[i][y].ship != null) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }

}
