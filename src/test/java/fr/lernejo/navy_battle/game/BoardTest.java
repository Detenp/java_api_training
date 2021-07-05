package fr.lernejo.navy_battle.game;

import fr.lernejo.navy_battle.models.Boat;
import fr.lernejo.navy_battle.models.Direction;
import fr.lernejo.navy_battle.models.ShootConsequence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BoardTest {
    @Test
    public void testAddBoatVertical() {
        int[][] expected = new int[][] {
            {1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        Board board = new Board();


        Assertions.assertTrue(board.addBoat(0, 0, Boat.PORTE_AVION, Direction.RIGHT));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Assertions.assertEquals(expected[i][j], board.board[i][j]);
            }
        }
    }

    @Test
    public void testAddBoatHorizontal() {
        int[][] expected = new int[][] {
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        Board board = new Board();

        Assertions.assertTrue(board.addBoat(0, 0, Boat.PORTE_AVION, Direction.DOWN));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Assertions.assertEquals(expected[i][j], board.board[i][j]);
            }
        }
    }

    @Test
    public void testMultipleAdds() {
        int[][] expected = new int[][] {
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 2, 2, 2, 2, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        Board board = new Board();

        Assertions.assertTrue(board.addBoat(0, 0, Boat.PORTE_AVION, Direction.DOWN));
        Assertions.assertTrue(board.addBoat(2, 3, Boat.CROISEUR, Direction.RIGHT));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Assertions.assertEquals(expected[i][j], board.board[i][j]);
            }
        }
    }

    @Test
    public void testAddsOutOfBounds() {
        int[][] expected = new int[][] {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        Board board = new Board();

        Assertions.assertFalse(board.addBoat(7, 0, Boat.PORTE_AVION, Direction.DOWN));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Assertions.assertEquals(expected[i][j], board.board[i][j]);
            }
        }
    }

    @Test
    void testOverlapAdd() {
        int[][] expected = new int[][] {
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        Board board = new Board();

        Assertions.assertTrue(board.addBoat(0, 0, Boat.PORTE_AVION, Direction.DOWN));
        Assertions.assertFalse(board.addBoat(0, 0, Boat.PORTE_AVION, Direction.RIGHT));
        Assertions.assertFalse(board.addBoat(7, 0, Boat.PORTE_AVION, Direction.DOWN));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Assertions.assertEquals(expected[i][j], board.board[i][j]);
            }
        }
    }

    @Test
    void testGetShotConsequenceHit() {
        int[][] expected = new int[][] {
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        Board board = new Board();
        board.addBoat(0, 0, Boat.PORTE_AVION, Direction.DOWN);
        Assertions.assertEquals(ShootConsequence.HIT, board.getShotConsequence(1, 0));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Assertions.assertEquals(expected[i][j], board.board[i][j]);
            }
        }
    }

    @Test
    void getShotConsequenceSinkTest() {
        int[][] expected = new int[][] {
            {-4, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {-4, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        Board board = new Board();
        board.addBoat(0, 0, Boat.TORPILLEUR, Direction.DOWN);
        Assertions.assertEquals(ShootConsequence.HIT, board.getShotConsequence(0, 0));
        Assertions.assertEquals(ShootConsequence.SUNK, board.getShotConsequence(1, 0));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Assertions.assertEquals(expected[i][j], board.board[i][j]);
            }
        }
    }

    @Test
    void didLoseTest() {
        Board board = new Board();
        board.addBoat(0, 0, Boat.TORPILLEUR, Direction.DOWN);
        Assertions.assertFalse(board.didLose());

        board.getShotConsequence(0, 0);
        board.getShotConsequence(1, 0);
        Assertions.assertTrue(board.didLose());
    }
}
