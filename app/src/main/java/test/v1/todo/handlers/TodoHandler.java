package test.v1.todo.handlers;

import com.sun.net.httpserver.HttpExchange;
import test.modules.json.JsonArray;
import test.modules.json.JsonObject;
import test.services.http.Response;
import test.v1.todo.controllers.TodoController;
import test.v1.todo.models.TodoItem;

import java.io.InputStream;

public class TodoHandler {


    public static Void handleGetRequest(HttpExchange exchange) {
        try {
            TodoItem[] todoItems = TodoController.getAll();
            JsonArray ja = new JsonArray(todoItems);
            Response.sendSuccess(exchange, ja);
        } catch (Exception e) {
            Response.sendError(exchange, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static Void handlePostRequest(HttpExchange exchange) {
        InputStream is = exchange.getRequestBody();
        try {
            String bodyString = new String(is.readAllBytes());
            JsonObject bodyParsed = new JsonObject(bodyString);
            TodoItem todoItem = TodoItem.from(bodyParsed);
            TodoController.insertOne(todoItem);
            Response.sendSuccess(exchange);
            is.close();
        } catch (Exception | Error e) {
            Response.sendError(exchange, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static Void handlePutRequest(HttpExchange exchange)  {
        try {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes());
            is.close();
            Response.send(exchange);
        } catch (Exception e) {
            e.printStackTrace();
            Response.sendError(exchange, e.getMessage());
        }
        return null;
    }
}
