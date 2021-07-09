package fr.lernejo.navy_battle.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fr.lernejo.navy_battle.game.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Start implements HttpHandler {
    private final String id;
    private final int port;
    private final Player[] players;

    public Start(String id, int port, Player[] players) {
        this.id = id;
        this.port = port;
        this.players = players;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("POST")) {
            sendResponse(404, "Not found", exchange);
            return;
        }

        JSONObject jo;

        try {
            jo = (JSONObject) (new JSONParser()).parse(readRequestBody(exchange));
        } catch (Exception e) {
            sendResponse(500, "Internal server error", exchange);
            return;
        }

        String reqId = (String) jo.get("id");
        String url = (String) jo.get("url");
        String message = (String) jo.get("message");

        if (reqId == null || url == null || message == null) {
            sendResponse(400, "Bad request", exchange);
            return;
        }

        players[1] = new Player(reqId, url);

        JSONObject responseJo = new JSONObject();

        responseJo.put("id", id);
        responseJo.put("url", "http://localhost:" + port);
        responseJo.put("message", "May the best code win");

        sendResponse(202, responseJo.toJSONString(), exchange);
    }

    private void sendResponse(int code, String message, HttpExchange exchange) throws IOException{
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, message.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes(StandardCharsets.UTF_8));
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
