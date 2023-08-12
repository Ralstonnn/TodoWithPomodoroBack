package test.router;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import test.services.http.HttpHandlerFactory;
import test.services.http.Response;
import test.v1.todo.handlers.TodoHandler;

public class Router {
    public static final String PREFIX = "/api/v1";
    public record routerItem(String path, HttpHandler handler) {}
    private static final routerItem[] routes = {
        new routerItem(PREFIX + "/todo", HttpHandlerFactory.httpHandlerGetPostPut("/todo", TodoHandler::handleGetRequest, TodoHandler::handlePostRequest, TodoHandler::handlePutRequest)),
        new routerItem(PREFIX + "/test", HttpHandlerFactory.httpHandlerGet("/test", Router::getHandlerTest))
    };

    public static routerItem[] getRouters() {
        return routes;
    }

    public static Void getHandlerTest(HttpExchange exchange) {
        try {
            Response.send(exchange, "Something in here to test get");
        } catch (Error | Exception e) {
            e.printStackTrace();
            Response.sendError(exchange, e.getMessage());
        }
        return null;
    }
}
