package test.services.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import test.modules.json.JsonArray;
import test.services.common.CommonServices;
import test.v1.todo.controllers.TodoController;
import test.v1.todo.models.TodoItem;

import java.util.HashMap;

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

    public static HashMap<String, Object> getQueryParams(HttpExchange exchange) throws Exception {
        HashMap<String, Object> result = new HashMap<>();
        String query = exchange.getRequestURI().getQuery();
        if (query == null) {
            throw new Exception("Empty query exception");
        }
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                if (CommonServices.isStringInstanceOfBoolean(entry[1])) {
                    result.put(entry[0], "true".equals(entry[1]));
                } else if (CommonServices.isStringInstanceOfNumber(entry[1])) {
                    result.put(entry[0], CommonServices.parseStringToNumber(entry[1]));
                } else {
                    result.put(entry[0], entry[1]);
                }
            }else {
                result.put(entry[0], "");
            }
        }
        return result;
    }
}
