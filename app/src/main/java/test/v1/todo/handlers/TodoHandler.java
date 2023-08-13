package test.v1.todo.handlers;

import com.sun.net.httpserver.HttpExchange;
import test.modules.json.JsonArray;
import test.modules.json.JsonObject;
import test.services.common.CommonServices;
import test.services.http.Response;
import test.v1.todo.controllers.TodoController;
import test.v1.todo.models.TodoItem;

import java.io.InputStream;

public class TodoHandler {
    public static Void handleGet(HttpExchange exchange) {
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
    public static Void handlePost(HttpExchange exchange) {
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
        CommonServices.closeInputStream(is);
        return null;
    }
    public static Void handlePut(HttpExchange exchange)  {
        InputStream is = exchange.getRequestBody();
        try {
            String bodyString = new String(is.readAllBytes());
            JsonObject bodyParsed = new JsonObject(bodyString);
            TodoItem todoItem = TodoItem.from(bodyParsed);
            TodoController.setIsDone(todoItem);
            Response.sendSuccess(exchange);
        } catch (Exception e) {
            e.printStackTrace();
            Response.sendError(exchange, e.getMessage());
        }
        CommonServices.closeInputStream(is);
        return null;
    }
    public static Void handleDelete(HttpExchange exchange) {
        InputStream is = exchange.getRequestBody();
        try {
            String bodyString = new String(is.readAllBytes());
            JsonObject bodyParsed = new JsonObject(bodyString);
            TodoItem todoItem = TodoItem.from(bodyParsed);
            TodoController.deleteOne(todoItem);
            Response.sendSuccess(exchange);
        } catch (Exception e) {
            e.printStackTrace();
            Response.sendError(exchange, e.getMessage());
        }
        CommonServices.closeInputStream(is);
        return null;
    }
}
