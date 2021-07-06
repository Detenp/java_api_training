package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpExchange;
import fr.lernejo.navy_battle.game.IAForTheWin;
import fr.lernejo.navy_battle.game.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class Launcher {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("Expected at least port.");
        }
        String portStr = args[0];
        int port = Integer.parseInt(portStr);

        if (args.length > 1) {
            String url = args[1];
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/api/game/start"))
                .setHeader("Accept","application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"id\":\"2\", \"url\":\"localhost:"
                    + port + "\", \"message\":\"hello\"}"))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jo;
            jo = (JSONObject) (new JSONParser()).parse(response.body());

            String reqId = (String) jo.get("id");
            String enemyUrl = (String) jo.get("url");
            String message = (String) jo.get("message");

            if (reqId == null || url == null || message == null) {
                return;
            }
            Player[] players = new Player[2];
            players[0] = new Player("2", "localhost:" + port);
            players[1] = new Player(reqId, enemyUrl);
            IAForTheWin ia = new IAForTheWin(players);
            Server server = new Server(port,"2", players, ia);
            server.start();

            System.out.println("Battle is about to start between " + players[0].getUrl() + " and " + players[1].getUrl());
            ia.shoot();
        }
        else {
            Player[] players = new Player[2];
            Server server = new Server(port,"1", players, new IAForTheWin(players));
            server.start();
        }
    }

    private String readRequestBody(HttpExchange exchange) throws IOException {
        StringBuilder buffer = new StringBuilder(512);
        try (InputStreamReader is = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(is)) {
            int b;
            while ((b = br.read()) != -1) {
                buffer.append((char) b);
            }
        }

        return buffer.toString();
    }
}
