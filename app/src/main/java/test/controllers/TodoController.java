package test.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.Document;
import test.services.database.MongoDb;
import test.services.http.Response;
import test.services.jsonParser.JsonBodyParser;

import java.io.IOException;
import java.io.InputStream;

public class TodoController {
    public static HttpHandler TodoController() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String requestMethod = exchange.getRequestMethod();

                switch (requestMethod) {
                    case "GET":
                        handleGetRequest(exchange);
                        break;
//                    case "POST":
//                        handlePostRequest(exchange);
//                        break;
                }
            }


            private static void handleGetRequest(HttpExchange exchange) throws IOException {
                Document[] documents = MongoDb.findAllInCollection("todo");
                String response = "";
                for (int i = 0, length = documents.length; i < length; i++) {
                    response += documents[i].toJson();
                    if (i < length - 1) {
                        response += ",";
                    }
                }
                Response.send(exchange, response);
                exchange.close();
            }

//            private static void handlePostRequest(HttpExchange exchange) throws IOException {
//                InputStream is = exchange.getRequestBody();
//                String body = new String(is.readAllBytes());
//                is.close();
//                JsonBodyParser jsonParsed = new JsonBodyParser(body);
//                System.out.println(jsonParsed.getValue(new String[]{"name", "value", "test"}));
//                Response.send(exchange);
//                exchange.close();
//            }
        };
    }
}
