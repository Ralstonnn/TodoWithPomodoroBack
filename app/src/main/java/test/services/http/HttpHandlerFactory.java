package test.services.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.function.Function;

public class HttpHandlerFactory {
    public static HttpHandler httpHandlerGet(String route, Function<HttpExchange, Void> getHandler) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!HttpValidation.validateRoute(String.valueOf(exchange.getRequestURI()), route)) {
                    Response.invalidRoute(exchange);
                    exchange.close();
                    return;
                }

                String requestMethod = exchange.getRequestMethod();
                Headers headers = exchange.getResponseHeaders();
                HttpCommon.setDefaultHeaders(headers);
                headers.set("Access-Control-Allow-Methods", "GET, OPTIONS");

                switch (requestMethod) {
                    case "OPTIONS":
                        Response.send(exchange);
                        break;
                    case "GET":
                        getHandler.apply(exchange);
                        break;
                }
                exchange.close();
            }
        };
    }
    public static HttpHandler httpHandlerGetPost(String route, Function<HttpExchange, Void> getHandler, Function<HttpExchange, Void> postHandler) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!HttpValidation.validateRoute(String.valueOf(exchange.getRequestURI()), route)) {
                    Response.invalidRoute(exchange);
                    exchange.close();
                    return;
                }

                String requestMethod = exchange.getRequestMethod();
                Headers headers = exchange.getResponseHeaders();
                HttpCommon.setDefaultHeaders(headers);
                headers.set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

                switch (requestMethod) {
                    case "OPTIONS":
                        Response.send(exchange);
                        break;
                    case "GET":
                        getHandler.apply(exchange);
                        break;
                    case "POST":
                        postHandler.apply(exchange);
                        break;
                }
                exchange.close();
            }
        };
    }
    public static HttpHandler httpHandlerGetPut(String route, Function<HttpExchange, Void> getHandler, Function<HttpExchange, Void> putHandler) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!HttpValidation.validateRoute(String.valueOf(exchange.getRequestURI()), route)) {
                    Response.invalidRoute(exchange);
                    exchange.close();
                    return;
                }

                String requestMethod = exchange.getRequestMethod();
                Headers headers = exchange.getResponseHeaders();
                HttpCommon.setDefaultHeaders(headers);
                headers.set("Access-Control-Allow-Methods", "GET, PUT, OPTIONS");

                switch (requestMethod) {
                    case "OPTIONS":
                        Response.send(exchange);
                        break;
                    case "GET":
                        getHandler.apply(exchange);
                        break;
                    case "PUT":
                        putHandler.apply(exchange);
                        break;
                }
                exchange.close();
            }
        };
    }
    public static HttpHandler httpHandlerGetDelete(String route, Function<HttpExchange, Void> getHandler, Function<HttpExchange, Void> deleteHandler) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!HttpValidation.validateRoute(String.valueOf(exchange.getRequestURI()), route)) {
                    Response.invalidRoute(exchange);
                    exchange.close();
                    return;
                }

                String requestMethod = exchange.getRequestMethod();
                Headers headers = exchange.getResponseHeaders();
                HttpCommon.setDefaultHeaders(headers);
                headers.set("Access-Control-Allow-Methods", "GET, DELETE, OPTIONS");

                switch (requestMethod) {
                    case "OPTIONS":
                        Response.send(exchange);
                        break;
                    case "GET":
                        getHandler.apply(exchange);
                        break;
                    case "DELETE":
                        deleteHandler.apply(exchange);
                        break;
                }
                exchange.close();
            }
        };
    }
    public static HttpHandler httpHandlerGetPostPut(String route, Function<HttpExchange, Void> getHandler, Function<HttpExchange, Void> postHandler, Function<HttpExchange, Void> putHandler) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!HttpValidation.validateRoute(String.valueOf(exchange.getRequestURI()), route)) {
                    Response.invalidRoute(exchange);
                    exchange.close();
                    return;
                }

                String requestMethod = exchange.getRequestMethod();
                Headers headers = exchange.getResponseHeaders();
                HttpCommon.setDefaultHeaders(headers);
                headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");

                switch (requestMethod) {
                    case "OPTIONS":
                        Response.send(exchange);
                        break;
                    case "GET":
                        getHandler.apply(exchange);
                        break;
                    case "POST":
                        postHandler.apply(exchange);
                        break;
                    case "PUT":
                        putHandler.apply(exchange);
                        break;
                }
                exchange.close();
            }
        };
    }
    public static HttpHandler httpHandlerGetPostDelete(String route, Function<HttpExchange, Void> getHandler, Function<HttpExchange, Void> postHandler, Function<HttpExchange, Void> deleteHandler) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!HttpValidation.validateRoute(String.valueOf(exchange.getRequestURI()), route)) {
                    Response.invalidRoute(exchange);
                    exchange.close();
                    return;
                }

                String requestMethod = exchange.getRequestMethod();
                Headers headers = exchange.getResponseHeaders();
                HttpCommon.setDefaultHeaders(headers);
                headers.set("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");

                switch (requestMethod) {
                    case "OPTIONS":
                        Response.send(exchange);
                        break;
                    case "GET":
                        getHandler.apply(exchange);
                        break;
                    case "POST":
                        postHandler.apply(exchange);
                        break;
                    case "DELETE":
                        deleteHandler.apply(exchange);
                        break;
                }
                exchange.close();
            }
        };
    }
    public static HttpHandler httpHandlerGetPostPutDelete(String route, Function<HttpExchange, Void> getHandler, Function<HttpExchange, Void> postHandler, Function<HttpExchange, Void> putHandler, Function<HttpExchange, Void> deleteHandler) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!HttpValidation.validateRoute(String.valueOf(exchange.getRequestURI()), route)) {
                    Response.invalidRoute(exchange);
                    exchange.close();
                    return;
                }

                String requestMethod = exchange.getRequestMethod();
                Headers headers = exchange.getResponseHeaders();
                HttpCommon.setDefaultHeaders(headers);
                headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

                switch (requestMethod) {
                    case "OPTIONS":
                        Response.send(exchange);
                        exchange.close();
                        break;
                    case "GET":
                        getHandler.apply(exchange);
                        exchange.close();
                        break;
                    case "POST":
                        postHandler.apply(exchange);
                        exchange.close();
                        break;
                    case "PUT":
                        putHandler.apply(exchange);
                        exchange.close();
                        break;
                    case "DELETE":
                        deleteHandler.apply(exchange);
                        exchange.close();
                        break;
                }
            }
        };
    }

    public static HttpHandler httpHandlerPost(String route, Function<HttpExchange, Void> postHandler) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!HttpValidation.validateRoute(String.valueOf(exchange.getRequestURI()), route)) {
                    Response.invalidRoute(exchange);
                    exchange.close();
                    return;
                }

                String requestMethod = exchange.getRequestMethod();
                Headers headers = exchange.getResponseHeaders();
                HttpCommon.setDefaultHeaders(headers);
                headers.set("Access-Control-Allow-Methods", "POST, OPTIONS");

                switch (requestMethod) {
                    case "OPTIONS":
                        Response.send(exchange);
                        break;
                    case "POST":
                        postHandler.apply(exchange);
                        break;
                }
                exchange.close();
            }
        };
    }
    public static HttpHandler httpHandlerPostPut(String route, Function<HttpExchange, Void> postHandler, Function<HttpExchange, Void> putHandler) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!HttpValidation.validateRoute(String.valueOf(exchange.getRequestURI()), route)) {
                    Response.invalidRoute(exchange);
                    exchange.close();
                    return;
                }

                String requestMethod = exchange.getRequestMethod();
                Headers headers = exchange.getResponseHeaders();
                HttpCommon.setDefaultHeaders(headers);
                headers.set("Access-Control-Allow-Methods", "POST, PUT, OPTIONS");

                switch (requestMethod) {
                    case "OPTIONS":
                        Response.send(exchange);
                        break;
                    case "POST":
                        postHandler.apply(exchange);
                        break;
                    case "PUT":
                        putHandler.apply(exchange);
                        break;
                }
                exchange.close();
            }
        };
    }
    public static HttpHandler httpHandlerPostDelete(String route, Function<HttpExchange, Void> postHandler, Function<HttpExchange, Void> deleteHandler) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!HttpValidation.validateRoute(String.valueOf(exchange.getRequestURI()), route)) {
                    Response.invalidRoute(exchange);
                    exchange.close();
                    return;
                }

                String requestMethod = exchange.getRequestMethod();
                Headers headers = exchange.getResponseHeaders();
                HttpCommon.setDefaultHeaders(headers);
                headers.set("Access-Control-Allow-Methods", "POST, DELETE, OPTIONS");

                switch (requestMethod) {
                    case "OPTIONS":
                        Response.send(exchange);
                        break;
                    case "POST":
                        postHandler.apply(exchange);
                        break;
                    case "DELETE":
                        deleteHandler.apply(exchange);
                        break;
                }
                exchange.close();
            }
        };
    }
    public static HttpHandler httpHandlerPostPutDelete(String route, Function<HttpExchange, Void> postHandler, Function<HttpExchange, Void> putHandler, Function<HttpExchange, Void> deleteHandler) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!HttpValidation.validateRoute(String.valueOf(exchange.getRequestURI()), route)) {
                    Response.invalidRoute(exchange);
                    exchange.close();
                    return;
                }

                String requestMethod = exchange.getRequestMethod();
                Headers headers = exchange.getResponseHeaders();
                HttpCommon.setDefaultHeaders(headers);
                headers.set("Access-Control-Allow-Methods", "POST, PUT, OPTIONS");

                switch (requestMethod) {
                    case "OPTIONS":
                        Response.send(exchange);
                        break;
                    case "POST":
                        postHandler.apply(exchange);
                        break;
                    case "PUT":
                        putHandler.apply(exchange);
                        break;
                    case "DELETE":
                        deleteHandler.apply(exchange);
                        break;
                }
                exchange.close();
            }
        };
    }

    public static HttpHandler httpHandlerPut(String route, Function<HttpExchange, Void> putHandler) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!HttpValidation.validateRoute(String.valueOf(exchange.getRequestURI()), route)) {
                    Response.invalidRoute(exchange);
                    exchange.close();
                    return;
                }

                String requestMethod = exchange.getRequestMethod();
                Headers headers = exchange.getResponseHeaders();
                HttpCommon.setDefaultHeaders(headers);
                headers.set("Access-Control-Allow-Methods", "PUT, OPTIONS");

                switch (requestMethod) {
                    case "OPTIONS":
                        Response.send(exchange);
                        break;
                    case "PUT":
                        putHandler.apply(exchange);
                        break;
                }
                exchange.close();
            }
        };
    }
    public static HttpHandler httpHandlerPutDelete(String route, Function<HttpExchange, Void> putHandler, Function<HttpExchange, Void> deleteHandler) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!HttpValidation.validateRoute(String.valueOf(exchange.getRequestURI()), route)) {
                    Response.invalidRoute(exchange);
                    exchange.close();
                    return;
                }

                String requestMethod = exchange.getRequestMethod();
                Headers headers = exchange.getResponseHeaders();
                HttpCommon.setDefaultHeaders(headers);
                headers.set("Access-Control-Allow-Methods", "PUT, DELETE, OPTIONS");

                switch (requestMethod) {
                    case "OPTIONS":
                        Response.send(exchange);
                        break;
                    case "PUT":
                        putHandler.apply(exchange);
                        break;
                    case "DELETE":
                        deleteHandler.apply(exchange);
                        break;
                }
                exchange.close();
            }
        };
    }

    public static HttpHandler httpHandlerDelete(String route, Function<HttpExchange, Void> deleteHandler) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!HttpValidation.validateRoute(String.valueOf(exchange.getRequestURI()), route)) {
                    Response.invalidRoute(exchange);
                    exchange.close();
                    return;
                }

                String requestMethod = exchange.getRequestMethod();
                Headers headers = exchange.getResponseHeaders();
                HttpCommon.setDefaultHeaders(headers);
                headers.set("Access-Control-Allow-Methods", "DELETE, OPTIONS");

                switch (requestMethod) {
                    case "OPTIONS":
                        Response.send(exchange);
                        break;
                    case "DELETE":
                        deleteHandler.apply(exchange);
                        break;
                }
                exchange.close();
            }
        };
    }
}
