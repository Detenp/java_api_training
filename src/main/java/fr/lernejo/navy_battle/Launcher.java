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
    public static void main(String[] args) throws Exception {
        if (args.length < 1) throw new IllegalArgumentException("Expected at least port.");

        int port = Integer.parseInt(args[0]);
        Player[] players = new Player[2];
        players[0] = new Player(port + "", "http://localhost:" + port);
        Server server = new Server(port, port + "", players, new IAForTheWin(players));
        server.start();

        if (args.length > 1) {
            String url = args[1];
            server.start();

            Utils utils = new Utils();

            HttpResponse<String> response =  utils.sendRequest(port, url);

            players[1] = utils.playerFromJSonResponse(response);

            System.out.println("Battle is about to start between " + players[0].getUrl() + " and " + players[1].getUrl());
            server.getIa().shoot();
        }
    }
}
