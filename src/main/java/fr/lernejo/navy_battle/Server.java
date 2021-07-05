package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpServer;
import fr.lernejo.navy_battle.game.Board;
import fr.lernejo.navy_battle.handlers.Fire;
import fr.lernejo.navy_battle.handlers.Ping;
import fr.lernejo.navy_battle.handlers.Start;
import fr.lernejo.navy_battle.models.Tuple;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Server {
    private final HttpServer httpInstance;
    private final Tuple<Board, Board> boards;

    public Server(int port, int id) throws IOException{
        this.boards = new Tuple<>(new Board(), new Board());
        HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        httpServer.setExecutor(Executors.newFixedThreadPool(1));
        httpServer.createContext("/ping", new Ping());
        httpServer.createContext("/api/game/start", new Start(id, port));
        httpServer.createContext("/api/game/fire", new Fire((Board) boards.getK()));
        this.httpInstance = httpServer;
    }

    public void start() {
        this.httpInstance.start();
    }

    public void close() {
        this.httpInstance.stop(0);
    }
}
