package test.v1.user.controllers;

import test.services.database.MariaDb;
import test.services.encryption.EncryptionService;
import test.v1.user.models.UserModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {
    public static void register(UserModel user) throws Exception {
        if (user.username.strip().length() == 0 || user.password.strip().length() == 0) {
            throw new Exception("Username and password must be filled");
        }
        String query = "INSERT INTO users (username, password) VALUE (\"%s\", \"%s\")".formatted(user.username, user.password);
        MariaDb.query(query);
    }

    public static UserModel login(String username, String password) throws Exception {
        UserModel user = getUserByUsername(username);
        String passwordEncrypted = EncryptionService.encryptHmacSha(password);
        if (!passwordEncrypted.equals(user.password)) {
            throw new Exception("Wrong username or password");
        }
        return user;
    }

    public static UserModel getUserByUsername(String username) throws Exception {
        String query = "SELECT * FROM users WHERE username=\"%s\"".formatted(username);
        ResultSet rs = MariaDb.query(query);
        return UserModel.from(rs);
    }

    public static UserModel getUserById(int id) throws Exception {
        String query = "SELECT * FROM users WHERE id=\"%d\"".formatted(id);
        ResultSet rs = MariaDb.query(query);
        return UserModel.from(rs);
    }
}
