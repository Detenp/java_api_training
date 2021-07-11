package fr.lernejo.navy_battle.handlers;

import com.sun.net.httpserver.HttpServer;
import fr.lernejo.navy_battle.game.Board;
import fr.lernejo.navy_battle.game.IAForTheWin;
import fr.lernejo.navy_battle.game.Player;
import fr.lernejo.navy_battle.game.handlers.FireHandlerForTest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;

public class
FireTest {

    @Test
    public void FireTest() throws Exception{
        int[][] expected = new int[][] {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        Player[] players = new Player[]{new Player("9876", "http://localhost:9876"),
            new Player("4567", "http://localhost:4567")};

        Board board = new Board(expected);
        IAForTheWin ia = new IAForTheWin(players, board);

        HttpServer httpServer1 = HttpServer.create(new InetSocketAddress("localhost", 9876), 0);
        httpServer1.setExecutor(Executors.newFixedThreadPool(1));
        httpServer1.createContext("/api/game/fire", new Fire(players, ia));
        httpServer1.start();

        HttpServer httpServer2 = HttpServer.create(new InetSocketAddress("localhost", 4567), 0);
        httpServer2.setExecutor(Executors.newFixedThreadPool(1));
        httpServer2.createContext("/api/game/fire", new FireHandlerForTest());
        httpServer2.start();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(players[0].getUrl() + "/api/game/fire?cell=E6"))
            .setHeader("Accept","application/json")
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject jo;

        jo = (JSONObject) (new JSONParser()).parse(response.body());

        String consequence = (String) jo.get("consequence");
        boolean shipLeft = (boolean) jo.get("shipLeft");

        Assertions.assertEquals("hit", consequence);
        Assertions.assertTrue(shipLeft);

        httpServer1.stop(0);
        httpServer2.stop(0);
    }
}
