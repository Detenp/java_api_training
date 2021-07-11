package fr.lernejo.navy_battle.game;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import fr.lernejo.navy_battle.game.handlers.FireHandlerForTest;
import fr.lernejo.navy_battle.handlers.Fire;
import fr.lernejo.navy_battle.models.ShootConsequence;
import fr.lernejo.navy_battle.models.Tuple;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

public class IAForTheWinTest {

    @Test
    public void placeBoatsTest() {
        int[] boatsCount = new int[4];

        IAForTheWin ia =  new IAForTheWin(new Player[2]);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (ia.getMyBoard().board[x][y] != 0) {
                    boatsCount[ia.getMyBoard().board[x][y] - 1] += 1;
                }
            }
        }

        int[] expected = new int[]{5, 4, 6, 2};
        Assertions.assertArrayEquals(expected, boatsCount);
    }

    @Test
    public void sendFireTest() throws Exception {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", 9876), 0);
        httpServer.setExecutor(Executors.newFixedThreadPool(1));
        httpServer.createContext("/api/game/fire", new FireHandlerForTest());
        httpServer.start();

        IAForTheWin ia = new IAForTheWin(new Player[2]);

        Tuple<ShootConsequence, Boolean> actual = ia.sendFire(5, 4, "http://localhost:9876");
        Assertions.assertEquals(ShootConsequence.HIT, actual.getK());
        Assertions.assertEquals(true, actual.getV());

        httpServer.stop(0);
    }

    @Test
    public void shootTest() throws Exception {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", 9876), 0);
        httpServer.setExecutor(Executors.newFixedThreadPool(1));
        httpServer.createContext("/api/game/fire", new FireHandlerForTest());
        httpServer.start();
        Player[] players = new Player[]{new Player("4567", "http://localhost:4567"),
            new Player("9876", "http://localhost:9876")};

        int[][] enemyBoard = new int[][] {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 0, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        IAForTheWin ia = new IAForTheWin(players, enemyBoard);

        Assertions.assertTrue(ia.shoot());
        httpServer.stop(0);
    }
}
