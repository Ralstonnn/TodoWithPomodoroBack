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

    public UserModel(ResultSet rs) {
        try {
            rs.next();
            this.id = rs.getInt("id");
            this.username = rs.getString("username");
            this.password = rs.getString("password");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public UserModel(JsonObject jo) {
        Object id = jo.getValue(new String[]{"id"});
        Object username = jo.getValue(new String[]{"username"});
        Object password = jo.getValue(new String[]{"password"});
        if (id instanceof Integer) this.id = (int) id;
        if (username instanceof String) this.username = (String) username;
        if (password instanceof String) this.password = (String) password;
    }
}
