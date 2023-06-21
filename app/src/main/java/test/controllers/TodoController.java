package test.controllers;

import com.mongodb.client.model.Updates;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import netscape.javascript.JSObject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonObject;
import org.bson.types.ObjectId;
import test.services.database.MongoDb;
import test.services.http.Response;
import test.services.jsonParser.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class TodoController {
    public static HttpHandler TodoController() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String requestMethod = exchange.getRequestMethod();

                Headers headers = exchange.getResponseHeaders();

                headers.add("Access-Control-Allow-Origin", "*");
                headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                headers.add("Content-type", "application/json");
                headers.add("Access-Control-Allow-Headers", "*");
                headers.add("Access-Control-Allow-Credentials", "true");
                headers.add("Access-Control-Allow-Credentials-Header", "*");

                switch (requestMethod) {
                    case "OPTIONS":
                        Response.send(exchange);
                        break;
                    case "GET":
                        handleGetRequest(exchange);
                        break;
//                    case "POST":
//                        handlePostRequest(exchange);
//                        break;
                    case "PUT":
                        handlePutRequest(exchange);
                        break;
                }
            }

            private static void handleGetRequest(HttpExchange exchange) throws IOException {
                Document[] documents = MongoDb.findAllInCollection("todo");

                String response = "[";
                for (int i = 0, length = documents.length; i < length; i++) {
                    response += documents[i].toJson();
                    if (i < length - 1) {
                        response += ",";
                    }
                }
                response += "]";

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

            private static void handlePutRequest(HttpExchange exchange) throws IOException {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes());
                is.close();
                HashMap<String, Object> response = new HashMap<>();
                response.put("success", true);

                try {
                    JsonParser jsonParsed = new JsonParser(body);
                    String id = jsonParsed.getValue(new String[]{"id"});
                    boolean isChecked = jsonParsed.getValue(new String[] {"isChecked"}).equalsIgnoreCase("true");

                    Document query = new Document().append("_id", new ObjectId(id));
                    Bson updates = Updates.set("isChecked", isChecked);
                    MongoDb.updateOne("todo", query, updates);
                }
                catch (Exception e) {
                    response.replace("response", "false");
                }

                Response.send(exchange, JsonParser.hashMapToJsonObject(response));
                exchange.close();
            }
        };
    }
}
