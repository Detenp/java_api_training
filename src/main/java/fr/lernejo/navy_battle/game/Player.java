package fr.lernejo.navy_battle.game;

public class Player {
    private final String id;
    private final String url;

    public Player(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }
}
