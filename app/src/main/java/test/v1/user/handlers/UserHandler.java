package test.v1.user.handlers;

import com.sun.net.httpserver.HttpExchange;
import test.modules.json.JsonObject;
import test.services.common.CommonServices;
import test.services.encryption.EncryptionService;
import test.services.http.Response;
import test.v1.user.controllers.UserController;
import test.v1.user.models.UserModel;

import java.io.InputStream;
import java.sql.SQLIntegrityConstraintViolationException;

public class UserHandler {
    public static Void registerHandler(HttpExchange exchange) {
        InputStream is = exchange.getRequestBody();
        try {
            String bodyString = new String(is.readAllBytes());
            JsonObject bodyParsed = new JsonObject(bodyString);
            UserModel user = UserModel.from(bodyParsed);
            user.password = EncryptionService.encryptHmacSha(user.password);
            UserController.register(user);

            user = UserController.getUserByUsername(user.username);
            if (user.id == -1) throw new Exception("User doesn't exist");

            JsonObject result = new JsonObject();
            String jwtToken = EncryptionService.generateJwt(new JsonObject("{\"sub\": %d}".formatted(user.id)));
            result.addKeyValue("profile", new JsonObject(user));
            result.addKeyValue("token", jwtToken);
            Response.sendSuccess(exchange, result);
        } catch (SQLIntegrityConstraintViolationException e) {
            Response.sendError(exchange, "User with this username already exists");
            e.printStackTrace();
        } catch (Exception | Error e) {
            Response.sendError(exchange, e.getMessage());
            e.printStackTrace();
        }
        CommonServices.closeInputStream(is);
        return null;
    }

    public static Void loginHandler(HttpExchange exchange) {
        InputStream is = exchange.getRequestBody();
        try {
            String bodyString = new String(is.readAllBytes());
            JsonObject bodyParsed = new JsonObject(bodyString);
            UserModel user = UserModel.from(bodyParsed);
            user = UserController.login(user.username, user.password);

            if (user.id == -1) throw new Exception("User doesn't exist");

            JsonObject result = new JsonObject();
            String jwtToken = EncryptionService.generateJwt(new JsonObject("{\"sub\": %d}".formatted(user.id)));
            result.addKeyValue("profile", new JsonObject(user));
            result.addKeyValue("token", jwtToken);
            Response.sendSuccess(exchange, result);
        } catch (Exception | Error e) {
            Response.sendError(exchange, e.getMessage());
            e.printStackTrace();
        }
        CommonServices.closeInputStream(is);
        return null;
    }
}
