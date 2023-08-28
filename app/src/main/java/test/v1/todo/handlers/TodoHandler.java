package test.v1.todo.handlers;

import com.sun.net.httpserver.HttpExchange;
import test.modules.json.JsonArray;
import test.modules.json.JsonObject;
import test.services.common.CommonServices;
import test.services.http.HttpCommon;
import test.services.http.Response;
import test.v1.todo.controllers.TodoController;
import test.v1.todo.models.TodoItem;

import java.io.InputStream;
import java.util.HashMap;

public class TodoHandler {
    public static Void handleGet(HttpExchange exchange) {
        try {
            int id = HttpCommon.getUserIdFromBearerToken(exchange);
            TodoItem[] todoItems = TodoController.getAllWithUserId(id);
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
            int userId = HttpCommon.getUserIdFromBearerToken(exchange);
            String bodyString = new String(is.readAllBytes());
            JsonObject bodyParsed = new JsonObject(bodyString);
            TodoItem todoItem = TodoItem.from(bodyParsed);
            TodoController.insertOne(todoItem, userId);
            TodoItem[] todoItems = TodoController.getAllWithUserId(userId);
            JsonArray ja = new JsonArray(todoItems);
            Response.sendSuccess(exchange, ja);
        } catch (Exception | Error e) {
            Response.sendError(exchange, e.getMessage());
            e.printStackTrace();
        }
        CommonServices.closeInputStream(is);
        return null;
    }

    public static Void handlePut(HttpExchange exchange) {
        InputStream is = exchange.getRequestBody();
        try {
            String bodyString = new String(is.readAllBytes());
            JsonObject bodyParsed = new JsonObject(bodyString);
            TodoItem todoItem = TodoItem.from(bodyParsed);
            boolean value = TodoController.setIsDone(todoItem);
            JsonObject data = new JsonObject();
            data.addKeyValue("value", value);
            Response.sendSuccess(exchange, data);
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
            int userId = HttpCommon.getUserIdFromBearerToken(exchange);
            HashMap<String, Object> qp = HttpCommon.getQueryParams(exchange);
            TodoItem todoItem = new TodoItem();
            todoItem.id = (int) qp.get("id");
            TodoController.deleteOne(todoItem);
            TodoItem[] todoItems = TodoController.getAllWithUserId(userId);
            JsonArray ja = new JsonArray(todoItems);
            Response.sendSuccess(exchange, ja);
        } catch (Exception e) {
            e.printStackTrace();
            Response.sendError(exchange, e.getMessage());
        }
        CommonServices.closeInputStream(is);
        return null;
    }

    public static Void handleDeleteDone(HttpExchange exchange) {
        InputStream is = exchange.getRequestBody();
        try {
            int userId = HttpCommon.getUserIdFromBearerToken(exchange);
            TodoController.deleteDone(userId);
            TodoItem[] todoItems = TodoController.getAllWithUserId(userId);
            JsonArray ja = new JsonArray(todoItems);
            Response.sendSuccess(exchange, ja);
        } catch (Exception e) {
            e.printStackTrace();
            Response.sendError(exchange, e.getMessage());
        }
        CommonServices.closeInputStream(is);
        return null;
    }
}
