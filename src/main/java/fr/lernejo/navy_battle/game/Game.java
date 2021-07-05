package fr.lernejo.navy_battle.game;

public class Game {
    private final Player p1;
    private final Player p2;
    private final IAForTheWin ia;
    private final Board board;

    Game(Player p1, Player p2, IAForTheWin ia, Board board) {
        this.p1 = p1;
        this.p2 = p2;

        this.ia = ia;
        this.board = board;
    }
}
