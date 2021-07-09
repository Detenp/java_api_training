package fr.lernejo.navy_battle.game;

import fr.lernejo.navy_battle.models.Boat;
import fr.lernejo.navy_battle.models.Direction;
import fr.lernejo.navy_battle.models.ShootConsequence;
import fr.lernejo.navy_battle.models.Tuple;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class IAForTheWin {
    private final Board myBoard;
    private final int[][] enemyBoard;
    private final Player[] players;

    public IAForTheWin(Player[] players) {
        this.myBoard = new Board();

        this.enemyBoard = new int[10][10];
        this.players = players;
        placeBoats();
    }

    private void placeBoats() {
        List<Boat> boats = new ArrayList<>(List.of(Boat.PORTE_AVION,
            Boat.CROISEUR,
            Boat.CONTRE_TORPILLEUR,
            Boat.CONTRE_TORPILLEUR,
            Boat.TORPILLEUR));

        for (Boat boat : boats) {
            int x, y;
            Direction dir;
            do {
                x = ThreadLocalRandom.current().nextInt(0, 10);
                y = ThreadLocalRandom.current().nextInt(0, 10);
                dir = Direction.values()[ThreadLocalRandom.current().nextInt(0, 2)];
            } while(!myBoard.addBoat(x, y, boat, dir));
        }
    }

    public boolean shoot() throws InterruptedException, ParseException, IOException {
        int x, y;
        do {
            x = (int) (Math.random() * 10);
            y = (int) (Math.random() * 10);
        } while (enemyBoard[x][y] == 1);
        enemyBoard[x][y] = 1;

        Tuple<ShootConsequence, Boolean> state = sendFire(x, y, players[1].getUrl());

        return (boolean) state.getV();
    }

    private Tuple<ShootConsequence, Boolean> sendFire(int x, int y, String url) throws InterruptedException, ParseException, IOException {
        HttpClient client = HttpClient.newHttpClient();
        String cell = (char) (y + 'A') + (char) (x + 1) + "";

        HttpResponse<String> response = sendFireRequest(cell, url);

        JSONObject jo;

        jo = (JSONObject) (new JSONParser()).parse(response.body());

        String consequence = (String) jo.get("consequence");
        boolean shipLeft = (boolean) jo.get("shipLeft");

        return new Tuple<>(getShootConsequenceFromStr(consequence), shipLeft);
    }

    private ShootConsequence getShootConsequenceFromStr(String s) {
        return switch (s) {
            case "sunk" -> ShootConsequence.SUNK;
            case "miss" -> ShootConsequence.MISS;
            case "hit" -> ShootConsequence.HIT;
            default -> throw new IllegalStateException("Unexpected value: " + s);
        };
    }

    public Board getMyBoard() {
        return myBoard;
    }

    private HttpResponse<String> sendFireRequest(String cell, String url) throws InterruptedException, IOException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url + "/api/game/fire?cell=" + cell))
            .setHeader("Accept","application/json")
            .GET()
            .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
