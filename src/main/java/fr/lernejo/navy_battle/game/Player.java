package fr.lernejo.navy_battle.game;

public class Player {
    private final int id;
    private final int port;

    public Player(int id, int port) {
        this.id = id;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public int getId() {
        return id;
    }
}
