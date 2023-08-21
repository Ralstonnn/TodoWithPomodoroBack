package test.http;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import test.exceptions.ConnectionFailedException;
import test.router.Router;
import test.services.Env;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private static HttpServer SERVER;
    private static final String HOSTNAME = Env.get("HOSTNAME");
    private static final int PORT = Integer.parseInt(Env.get("PORT"));

    public static void init() throws ConnectionFailedException {
        System.out.println("Initializing HTTP server...");
        try {
            SERVER = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), 0);
            System.out.println("Server started successfully at: http://" + HOSTNAME + ":" + PORT);
        } catch (IOException e) {
            throw new ConnectionFailedException("Server could not be initialized");
        }
    }

    public static void start() {
        SERVER.start();
    }

    public static void addRouters(Router.routerItem[] routes) {
        for (Router.routerItem route : routes) {
            addHttpHandler(route.path(), route.handler());
        }
    }

    public static void addHttpHandler(String path, HttpHandler handler) {
        SERVER.createContext(path, handler);
    }
}
