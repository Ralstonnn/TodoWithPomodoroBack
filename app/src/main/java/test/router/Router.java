package test.router;

import com.sun.net.httpserver.HttpHandler;
import test.services.http.HttpHandlerFactory;
import test.v1.todo.handlers.TodoHandler;

public class Router {
    public static final String PREFIX = "/api/v1";

    public record routerItem(String path, HttpHandler handler) {
    }

    private static final routerItem[] routes = {
            new routerItem(PREFIX + "/todo", HttpHandlerFactory.httpHandlerGetPostPutDelete("/todo", TodoHandler::handleGet, TodoHandler::handlePost, TodoHandler::handlePut, TodoHandler::handleDelete)),
            new routerItem(PREFIX + "/todo/delete-done", HttpHandlerFactory.httpHandlerDelete("/todo/delete-done", TodoHandler::handleDeleteDone))
    };

    public static routerItem[] getRouters() {
        return routes;
    }
}
