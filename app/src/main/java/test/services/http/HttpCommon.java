package test.services.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import test.modules.json.JsonArray;
import test.v1.todo.controllers.TodoController;
import test.v1.todo.models.TodoItem;

public class HttpCommon {
    public static void setDefaultHeaders(Headers headers) {
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.add("Content-type", "application/json");
        headers.add("Access-Control-Allow-Headers", "*");
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Credentials-Header", "*");
    }

    public static String[][] getDefaultHeaders() {
        return new String[][] {
            {"Access-Control-Allow-Origin", "*"},
            {"Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"},
            {"Content-type", "application/json"},
            {"Access-Control-Allow-Headers", "*"},
            {"Access-Control-Allow-Credentials", "true"},
            {"Access-Control-Allow-Credentials-Header", "*"}
        };
    }
}
