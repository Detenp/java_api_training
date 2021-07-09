package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpExchange;
import fr.lernejo.navy_battle.game.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class Utils {
    public HttpResponse<String> sendRequest(int port, String url) throws IOException, InterruptedException {
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

        return response;
    }

    public Player playerFromJSonResponse(HttpResponse<String> response) throws ParseException, IllegalArgumentException {
        JSONObject jo = (JSONObject) (new JSONParser()).parse(response.body());

        String reqId = (String) jo.get("id");
        String enemyUrl = (String) jo.get("url");
        String message = (String) jo.get("message");

        if (reqId == null || enemyUrl == null || message == null) {
            throw new IllegalArgumentException();
        }
        return new Player(reqId, enemyUrl);
    }

    public void sendResponse(int code, String message, HttpExchange exchange) throws IOException{
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, message.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
