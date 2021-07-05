package fr.lernejo.navy_battle.handlers;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.Executors;

class StartTest {

    @Test
    public void handleNotPost() throws Exception {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", 9876), 0);
        httpServer.setExecutor(Executors.newFixedThreadPool(1));
        httpServer.createContext("/start", new Start(1, 4567));
        httpServer.start();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:9876/start"))
            .setHeader("Accept","application/json")
            .GET()
            .build();

        HttpResponse<String> actual = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertThat(actual.statusCode()).isEqualTo(404);
        Assertions.assertThat(actual.body().getBytes()).isEqualTo("Not found".getBytes(StandardCharsets.UTF_8));

        httpServer.stop(0);
    }

    @Test
    void handleBadRequest() throws Exception {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", 9876), 0);
        httpServer.setExecutor(Executors.newFixedThreadPool(1));
        httpServer.createContext("/start", new Start(1, 4567));
        httpServer.start();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:9876/start"))
            .setHeader("Accept","application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\"url\":\"http://localhost:4567\", \"message\":\"hello\"}"))
            .build();

        HttpResponse<String> actual = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertThat(actual.statusCode()).isEqualTo(400);
        Assertions.assertThat(actual.body().getBytes()).isEqualTo("Bad request".getBytes(StandardCharsets.UTF_8));

        httpServer.stop(0);
    }

    @Test
    void handleGoodRequestTest() throws Exception {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", 9876), 0);
        httpServer.setExecutor(Executors.newFixedThreadPool(1));
        httpServer.createContext("/start", new Start(1, 4567));
        httpServer.start();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:9876/start"))
            .setHeader("Accept","application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\"id\":\"2\", \"url\":\"http://localhost:4567\", \"message\":\"hello\"}"))
            .build();

        HttpResponse<String> actual = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertThat(actual.statusCode()).isEqualTo(202);

        httpServer.stop(0);
    }
}
