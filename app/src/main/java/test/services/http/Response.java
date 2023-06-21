package test.services.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class Response {
    public static void send(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, 0);
    }
    public static void send(HttpExchange exchange, int responseCode) throws IOException {
        exchange.sendResponseHeaders(responseCode, 0);
    }
    public static void send(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, 0);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    public static void send(HttpExchange exchange, String response, int responseCode) throws IOException {
        exchange.sendResponseHeaders(responseCode, 0);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
