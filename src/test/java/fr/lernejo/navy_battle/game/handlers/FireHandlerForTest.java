package fr.lernejo.navy_battle.game.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class FireHandlerForTest implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String cell = exchange.getRequestURI().getQuery().split("=")[1];
        Assertions.assertEquals('E', cell.charAt(0));
        Assertions.assertEquals("E6", cell);

        JSONObject responseJo = new JSONObject();
        responseJo.put("consequence", "hit");
        responseJo.put("shipLeft", true);

        String message = responseJo.toString();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, message.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
