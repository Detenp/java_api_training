package fr.lernejo.navy_battle;

import fr.lernejo.navy_battle.game.Board;
import fr.lernejo.navy_battle.game.IAForTheWin;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Launcher {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("Expected at least port.");
        }
        String portStr = args[0];
        int port = Integer.parseInt(portStr);

        if (args.length > 1) {
            String url = args[1];
            Server server = new Server(port,2);
            server.start();
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/api/game/start"))
                .setHeader("Accept","application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"id\":\"2\", \"url\":\"localhost:"
                    + port + "\", \"message\":\"hello\"}"))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Board myBoard = new Board();
            IAForTheWin ia = new IAForTheWin(myBoard, url);

            while (true) {
                if (!ia.shootLoop()) break;
            }

            server.close();
        }
        else {
            Server server = new Server(port,1);
            server.start();
        }
    }
}
