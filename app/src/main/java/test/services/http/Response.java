package test.services.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import test.modules.json.JsonObject;

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

    public static void sendSuccess(HttpExchange exchange) throws IOException {
        JsonObject response = new JsonObject();
        response.addKeyValue("success", true);
        exchange.sendResponseHeaders(200, 0);
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
    public static void sendSuccess(HttpExchange exchange, Object data) throws IOException {
        JsonObject response = new JsonObject();
        response.addKeyValue("success", true);
        response.addKeyValue("data", data);
        exchange.sendResponseHeaders(200, 0);
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }

    public static void sendError(HttpExchange exchange) throws IOException {
        JsonObject response = new JsonObject();
        response.addKeyValue("error", true);
        exchange.sendResponseHeaders(200, 0);
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
    public static void sendError(HttpExchange exchange, String errorMessage) throws IOException {
        JsonObject response = new JsonObject();
        response.addKeyValue("error", true);
        response.addKeyValue("message", errorMessage);
        exchange.sendResponseHeaders(200, 0);
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
}
