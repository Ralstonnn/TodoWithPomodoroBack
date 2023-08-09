package test.v1.todo.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import test.modules.json.JsonArray;
import test.modules.json.JsonObject;
import test.services.database.MariaDb;
import test.services.http.HttpCommon;
import test.services.http.HttpValidation;
import test.services.http.Response;
import test.v1.todo.controllers.TodoController;
import test.v1.todo.models.TodoItem;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;

public class TodoHandler {
    private static String ROUTE = "/todo";

    public static HttpHandler create() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!HttpValidation.validateRoute(String.valueOf(exchange.getRequestURI()), ROUTE)) {
                    Response.invalidRoute(exchange);
                    exchange.close();
                    return;
                }

                String requestMethod = exchange.getRequestMethod();
                Headers headers = exchange.getResponseHeaders();
                HttpCommon.setDefaultHeaders(headers);

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
                    TodoItem[] todoItems = TodoController.getAll();
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
                    TodoItem todoItem = TodoItem.from(bodyParsed);
                    TodoController.insertOne(todoItem);
                    Response.sendSuccess(exchange);
                } catch (Exception | Error e) {
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
