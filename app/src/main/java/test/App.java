/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import test.http.Server;
import test.services.database.MongoDb;
import test.services.jsonParser.JsonBodyParser;
import test.services.http.Response;

import java.io.*;
import java.util.HashMap;

public class App {
    public static void main(String[] args) {
        // Init database connection
        try {
            init();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Server.addHttpHandler("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String requestMethod = exchange.getRequestMethod();

                switch (requestMethod) {
                    case "GET":
                        handleGetRequest(exchange);
                        break;
                    case "POST":
                        handlePostRequest(exchange);
                        break;
                }
            }

            private static void handleGetRequest(HttpExchange exchange) throws IOException {
                Response.send(exchange, "Get request received");
                exchange.close();
            }

            private static void handlePostRequest(HttpExchange exchange) throws IOException {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes());
                is.close();
                JsonBodyParser jsonParsed = new JsonBodyParser(body);
                System.out.println(jsonParsed.getValue(new String[]{"name", "value", "test"}));
                Response.send(exchange);
                exchange.close();
            }
        });

        Server.start();
    }

    private static void init() throws Exception {
        MongoDb.init();
        System.out.println("Connected to database successfully");
        Server.init();
    }
}
