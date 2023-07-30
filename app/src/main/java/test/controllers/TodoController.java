package test.controllers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import test.models.TodoItem;
import test.modules.json.JsonArray;
import test.modules.json.JsonObject;
import test.services.database.MariaDb;
import test.services.http.Response;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;

public class TodoController {
    public static HttpHandler TodoController() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String requestMethod = exchange.getRequestMethod();

                Headers headers = exchange.getResponseHeaders();

                headers.add("Access-Control-Allow-Origin", "*");
                headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                headers.add("Content-type", "application/json");
                headers.add("Access-Control-Allow-Headers", "*");
                headers.add("Access-Control-Allow-Credentials", "true");
                headers.add("Access-Control-Allow-Credentials-Header", "*");

                switch (requestMethod) {
                    case "OPTIONS":
                        Response.send(exchange);
                        break;
                    case "GET":
                        handleGetRequest(exchange);
                        break;
                    case "POST":
                        handlePostRequest(exchange);
                        break;
//                    case "PUT":
//                        handlePutRequest(exchange);
//                        break;
                }
            }

            private static void handleGetRequest(HttpExchange exchange) throws IOException {
                try {
                    ResultSet rs = MariaDb.query("SELECT * FROM todo_item");
                    TodoItem[] todoItems = TodoItem.from(rs);
                    JsonArray ja = new JsonArray(todoItems);
                    Response.sendSuccess(exchange, ja);
                } catch (Exception e) {
                    Response.sendError(exchange, e.getMessage());
                    e.printStackTrace();
                }

                exchange.close();
            }

            private static void handlePostRequest(HttpExchange exchange) throws IOException {
                InputStream is = exchange.getRequestBody();
                try {
                    String bodyString = new String(is.readAllBytes());
                    JsonObject bodyParsed = new JsonObject(bodyString);
                    String text = "\"" + bodyParsed.getValue(new String[]{"text"}) + "\"";
                    boolean isDone = (boolean) bodyParsed.getValue(new String[]{"is_done"});
                    String query = "INSERT INTO todo_item (text, is_done) VALUE (%s, %b)".formatted(text, isDone);
                    MariaDb.query(query);
                    Response.sendSuccess(exchange);
                } catch (Exception e) {
                    Response.sendError(exchange, e.getMessage());
                    e.printStackTrace();
                }
                is.close();
                exchange.close();
            }

            private static void handlePutRequest(HttpExchange exchange) throws IOException {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes());
                is.close();
                Response.send(exchange);
                exchange.close();
            }
        };
    }
}
