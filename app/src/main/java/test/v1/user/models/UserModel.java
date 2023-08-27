package test.v1.user.models;

import test.modules.json.JsonObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {
    public int id = -1;
    public String username;
    public String password;

    public UserModel() {
        this.username = "";
        this.password = "";
    }

    public static UserModel from(ResultSet rs) {
        UserModel user = new UserModel();
        try {
            rs.next();
            user.id = rs.getInt("id");
            user.username = rs.getString("username");
            user.password = rs.getString("password");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return user;
    }

    public static UserModel from(JsonObject jo) {
        UserModel user = new UserModel();
        Object id = jo.getValue(new String[]{"id"});
        Object username = jo.getValue(new String[]{"username"});
        Object password = jo.getValue(new String[]{"password"});
        if (id instanceof Integer) user.id = (int) id;
        if (username instanceof String) user.username = (String) username;
        if (password instanceof String) user.password = (String) password;
        if (password instanceof Integer) user.password = ((Integer) password).toString();
        return user;
    }
}
