package test.router;

import com.sun.net.httpserver.HttpHandler;
import test.controllers.TodoController;

public class Router {
    public record routerItem(String path, HttpHandler handler) {}
    private static final routerItem[] routes = {new routerItem("/todo", TodoController.TodoController())};

    public static routerItem[] getRouters() {
        return routes;
    }
}
