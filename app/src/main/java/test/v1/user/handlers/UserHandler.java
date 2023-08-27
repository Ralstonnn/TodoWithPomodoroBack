package test.v1.user.handlers;

import com.sun.net.httpserver.HttpExchange;
import test.modules.json.JsonObject;
import test.services.common.CommonServices;
import test.services.http.Response;
import test.v1.user.models.UserModel;

import java.io.InputStream;

public class UserHandler {
    public static Void registerHandler(HttpExchange exchange) {
        InputStream is = exchange.getRequestBody();
        try {
            String bodyString = new String(is.readAllBytes());
            JsonObject bodyParsed = new JsonObject(bodyString);
            UserModel user = UserModel.from(bodyParsed);
            Response.sendSuccess(exchange);
        } catch (Exception | Error e) {
            Response.sendError(exchange, e.getMessage());
            e.printStackTrace();
        }
        CommonServices.closeInputStream(is);
        return null;
    }
}
