package fr.lernejo.navy_battle.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fr.lernejo.navy_battle.game.IAForTheWin;
import fr.lernejo.navy_battle.game.Player;
import fr.lernejo.navy_battle.models.ShootConsequence;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Fire implements HttpHandler {
    private final Player[] players;
    private final IAForTheWin ia;

    public Fire(Player[] players, IAForTheWin ia) {
        this.players = players;
        this.ia = ia;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("GET")) {
            sendResponse(404, "Not found", exchange);
            return;
        }

        String cell = exchange.getRequestURI().getQuery().split("=")[1];
        int y = cell.charAt(0) - 'A';
        int x;
        try {
            x = Integer.parseInt(cell.substring(1)) - 1;
        } catch (NumberFormatException e) {
            sendResponse(400, "Bad request", exchange);
            return;
        }


        ShootConsequence consequence = ia.getMyBoard().getShotConsequence(x, y);
        String strConsequence = switch (consequence) {
            case SUNK -> "sunk";
            case HIT -> "hit";
            case MISS -> "miss";
        };
        boolean shipLeft = !ia.getMyBoard().didLose();

        JSONObject responseJo = new JSONObject();

        System.out.println(players[1].getId() + " shot on " + cell + " and " + strConsequence);

        responseJo.put("consequence", strConsequence);
        responseJo.put("shipLeft", shipLeft);

        sendResponse(202, responseJo.toJSONString(), exchange);

        if (shipLeft) {

            boolean isOver;
            try {
                isOver = !ia.shoot();
            } catch (InterruptedException | ParseException e) {
                sendResponse(500, "Internal server error.", exchange);
                return;
            }

            if (isOver) {
                System.out.println("Player " + players[0].getId() + " won!");
            }
        }
        else {
            System.out.println("Player " + players[1].getId() + " won!");
        }
    }

    private void sendResponse(int code, String message, HttpExchange exchange) throws IOException{
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, message.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
