package fr.lernejo.navy_battle.handlers;

import com.sun.net.httpserver.HttpServer;
import fr.lernejo.navy_battle.game.Player;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;

public class PingTest {
    @Test
    public void pingTest() throws Exception {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", 9876), 0);
        httpServer.setExecutor(Executors.newFixedThreadPool(1));
        httpServer.createContext("/ping", new Ping());
        httpServer.start();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:9876/ping"))
            .setHeader("Accept","application/json")
            .GET()
            .build();

        HttpResponse<String> actual = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertThat(actual.statusCode()).isEqualTo(200);

        httpServer.stop(0);
    }
}
