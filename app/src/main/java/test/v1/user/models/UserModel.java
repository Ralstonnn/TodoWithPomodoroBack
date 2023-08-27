package test.v1.user.models;

import test.modules.json.JsonObject;

public class UserModel {
    private int id = -1;
    private String username;
    private String password;

    public UserModel() {
        this.username = "";
        this.password = "";
    }

    public static UserModel from(JsonObject jo) {
        UserModel user = new UserModel();
        Object id = jo.getValue(new String[]{"id"});
        Object username = jo.getValue(new String[]{"username"});
        Object password = jo.getValue(new String[]{"password"});
        if (id instanceof Integer) user.id = (int) id;
        if (username instanceof String) user.username = (String) username;
        if (password instanceof String) user.password = (String) password;
        return user;
    }
}
