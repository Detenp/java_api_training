package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpExchange;
import fr.lernejo.navy_battle.game.IAForTheWin;
import fr.lernejo.navy_battle.game.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class Launcher {
    /*
    public static void main(String[] args) throws Exception {
        if (args.length < 1) throw new IllegalArgumentException("Expected at least port.");
        int port = Integer.parseInt(args[0]);
        Player[] players = new Player[2];
        players[0] = new Player(port + "", "http://localhost:" + port);
        Server server = new Server(port, port + "", players, new IAForTheWin(players));
        server.start();
        if (args.length > 1) {
            String url = args[1];
            boolean success = false; HttpResponse<String> response = null;
            do {
                try {
                    response =  new Utils().sendRequest(port, url); success = true;
                } catch (ConnectException ignored){}
            } while(!success);
            players[1] = new Utils().playerFromJSonResponse(response);
            server.getIa().shoot();
        }
    }*/

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("Expected at least port.");
        }
        String portStr = args[0];
        int port = Integer.parseInt(portStr);

        if (args.length > 1) {
            String url = args[1];
            Player[] players = new Player[2];
            players[0] = new Player(port + "", "http://localhost:" + port);
            IAForTheWin ia = new IAForTheWin(players);
            Server server = new Server(port,port + "", players, ia);
            server.start();

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/api/game/start"))
                .setHeader("Accept","application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"id\":\"" + port + "\", \"url\":\"http://localhost:"
                    + port + "\", \"message\":\"hello\"}"))
                .build();

            HttpResponse<String> response = null;
            boolean success = false;
            do {
                try {
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    success = true;
                } catch(ConnectException ignored){}
            } while(!success);

            JSONObject jo;
            jo = (JSONObject) (new JSONParser()).parse(response.body());

            String reqId = (String) jo.get("id");
            String enemyUrl = (String) jo.get("url");
            String message = (String) jo.get("message");

            if (reqId == null || url == null || message == null) {
                return;
            }

            players[1] = new Player(reqId, enemyUrl);

            System.out.println("Battle is about to start between " + players[0].getUrl() + " and " + players[1].getUrl());
            ia.shoot();
        }
        else {
            Player[] players = new Player[2];
            players[0] = new Player(port + "", "http://localhost:" + port);
            Server server = new Server(port,port + "", players, new IAForTheWin(players));
            server.start();
        }
    }
}
