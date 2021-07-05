package fr.lernejo.navy_battle.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fr.lernejo.navy_battle.game.Board;
import fr.lernejo.navy_battle.models.ShootConsequence;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Fire implements HttpHandler {
    private final Board board;

    public Fire(Board board) {
        this.board = board;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("GET")) {
            sendResponse(404, "Not found", exchange);
            return;
        }

        String cell = exchange.getRequestURI().getQuery().split("=")[1];
        int x = cell.toLowerCase().charAt(0) - 'a';
        int y;
        try {
            y = Integer.parseInt(cell.substring(1));
        } catch (NumberFormatException e) {
            sendResponse(400, "Bad request", exchange);
            return;
        }

        ShootConsequence consequence = board.getShotConsequence(x, y);
        String strConsequence = switch (consequence) {
            case SUNK -> "sunk";
            case HIT -> "miss";
            case MISS -> "hit";
        };
        boolean shipLeft = !board.didLose();

        JSONObject responseJo = new JSONObject();

        responseJo.put("consequence", consequence);
        responseJo.put("shipLeft", shipLeft);

        sendResponse(202, responseJo.toJSONString(), exchange);
    }

    private void sendResponse(int code, String message, HttpExchange exchange) throws IOException{
        exchange.sendResponseHeaders(code, message.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
