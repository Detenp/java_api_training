package fr.lernejo.navy_battle.game;

import fr.lernejo.navy_battle.models.Boat;
import fr.lernejo.navy_battle.models.Direction;
import fr.lernejo.navy_battle.models.ShootConsequence;

public class Board {
    protected final int[][] board;
    private final int emptyValue;

    public Board() {
        this.board = new int[10][10];
        this.emptyValue = 0;
    }

    public Board(int[][] board) {
        this.board = board;
        this.emptyValue = 0;
    }

    public boolean didLose() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j] > 0) return false;
            }
        }
        return true;
    }

    public boolean addBoat(int x, int y, Boat boat, Direction dir) {
        if (x + boat.size() > 10 || y + boat.size() > 10) return false;
        if (dir == Direction.DOWN) {
            return addBoatDown(x, y, boat);
        }
        return addBoatRight(x, y, boat);
    }

    private boolean addBoatRight(int x, int y, Boat boat) {
        for (int i = 0; i < boat.size(); i++) {
            if (board[x][y + i] != emptyValue || y > 10) return false;
        }

        for (int i = 0; i < boat.size(); i++) {
            board[x][y + i] = boat.getValue();
        }
        return true;
    }

    private boolean addBoatDown(int x, int y, Boat boat) {
        for (int i = 0; i < boat.size(); i++) {
            if (board[x + i][y] != emptyValue || x > 10) return false;
        }

        for (int i = 0; i < boat.size(); i++) {
            board[x + i][y] = boat.getValue();
        }
        return true;
    }

    public ShootConsequence getShotConsequence(int x, int y) {
        if (board[x][y] != emptyValue) {
            if (board[x][y] < 0) {
                return ShootConsequence.MISS;
            }
            board[x][y] = - board[x][y];
            return didSink(x, y) ? ShootConsequence.SUNK : ShootConsequence.HIT;
        }
        return ShootConsequence.MISS;
    }

    private boolean didSink(int x, int y) {
        Boat boat = Boat.values()[Math.abs(board[x][y]) - 1];
        return checkBoat(x, y, boat, Direction.RIGHT)
            && checkBoat(x, y, boat, Direction.DOWN)
            && checkBoat(x, y, boat, Direction.LEFT)
            && checkBoat(x, y, boat, Direction.UP);
    }

    private boolean checkBoat(int x, int y, Boat boat, Direction dir) {
        if (x < 0 || x >= 10 || y < 0 || y >= 10)
            return true;
        if (board[x][y] == boat.getValue()) return false;
        if (board[x][y] == - boat.getValue()) {
            if (dir == Direction.RIGHT) {
                return checkBoat(x, y + 1, boat, dir);
            } else if (dir == Direction.UP){
                 return checkBoat(x - 1, y, boat, dir);
            } else if (dir == Direction.LEFT) {
                 return checkBoat(x, y - 1, boat, dir);
            } else {
                return checkBoat(x + 1, y, boat, dir);
            }
        }
        return true;
    }
}
