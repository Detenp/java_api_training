package fr.lernejo.navy_battle.game;

import com.sun.net.httpserver.HttpExchange;
import fr.lernejo.navy_battle.models.Boat;
import fr.lernejo.navy_battle.models.Direction;
import fr.lernejo.navy_battle.models.ShootConsequence;
import fr.lernejo.navy_battle.models.Tuple;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class IAForTheWin {
    private final Board myBoard;
    private final int[][] enemyBoard;
    private final String url;

    public IAForTheWin(Board myBoard, String url) {
        this.myBoard = myBoard;
        this.url = url;

        this.enemyBoard = new int[10][10];
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

    public boolean shootLoop() throws Exception {
        int x, y;
        do {
            x = ThreadLocalRandom.current().nextInt(0, 10);
            y = ThreadLocalRandom.current().nextInt(0, 10);
        } while (enemyBoard[x][y] == 1);

        Tuple<ShootConsequence, Boolean> state = sendFire(x, y, url);
        Direction dir = Direction.values()[ThreadLocalRandom.current().nextInt(0, 3)];
        while ((boolean) state.getV() && state.getK() == ShootConsequence.HIT) {
            enemyBoard[x][y] = -1;
            switch (dir) {
                case UP -> x = x - 1;
                case DOWN -> x = x + 1;
                case LEFT -> y = y - 1;
                case RIGHT -> y = y + 1;
            }

            state = sendFire(x, y, url);
        }

       return (boolean) state.getV();
    }

    private Tuple<ShootConsequence, Boolean> sendFire(int x, int y, String url) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url + "/api/game/fire?cell=" + (x + 'A') + y))
            .setHeader("Accept","application/json")
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

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
}
