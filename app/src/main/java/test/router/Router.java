package test.router;

import com.sun.net.httpserver.HttpHandler;
import test.services.http.HttpHandlerFactory;
import test.v1.todo.handlers.TodoHandler;
import test.v1.user.handlers.UserHandler;

public class Router {
    public static final String PREFIX = "/api/v1";

    public record routerItem(String path, HttpHandler handler) {
    }

    private static final routerItem[] routes = {
            new routerItem(
                    PREFIX + "/todo",
                    HttpHandlerFactory.httpHandlerGetPostPutDeleteAuthorized(
                            "/todo",
                            TodoHandler::handleGet,
                            TodoHandler::handlePost,
                            TodoHandler::handlePut,
                            TodoHandler::handleDelete
                    )
            ),
            new routerItem(
                    PREFIX + "/todo/delete-done",
                    HttpHandlerFactory.httpHandlerDeleteAuthorized(
                            "/todo/delete-done",
                            TodoHandler::handleDeleteDone
                    )
            ),
            new routerItem(
                    PREFIX + "/user/register",
                    HttpHandlerFactory.httpHandlerPost(
                            "/user/register",
                            UserHandler::registerHandler
                    )
            ),
            new routerItem(
                    PREFIX + "/user/login",
                    HttpHandlerFactory.httpHandlerPost(
                            "/user/login",
                            UserHandler::loginHandler
                    )
            ),
            new routerItem(
                    PREFIX + "/user/profile",
                    HttpHandlerFactory.httpHandlerGetAuthorized(
                            "/user/profile",
                            UserHandler::profileGetHandler
                    )
            ),
    };

    public static routerItem[] getRouters() {
        return routes;
    }
}
