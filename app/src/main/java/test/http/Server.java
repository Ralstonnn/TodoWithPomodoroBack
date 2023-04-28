package test.http;

import com.sun.net.httpserver.HttpServer;
import test.services.Env;

import java.net.InetSocketAddress;

public class Server {
    private static HttpServer SERVER;
    private static final String HOSTNAME = Env.get("HOSTNAME");
    private static final int PORT = Integer.parseInt(Env.get("PORT"));

    public static void init() {
        System.out.println("Initializing HTTP server...");
//        SERVER = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), 0);
    }
}
