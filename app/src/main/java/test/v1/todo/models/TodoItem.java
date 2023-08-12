package test.v1.todo.models;

import test.modules.json.JsonObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TodoItem {
    public int id = -1;
    public String text;
    public boolean isDone = false;

    public TodoItem() {
        this.text = "";
        this.isDone = false;
    }

    public static TodoItem[] from(ResultSet rs) {
        ArrayList<TodoItem> todoItems = new ArrayList<>();
        try {
            while (rs.next()) {
                TodoItem item = new TodoItem();
                item.id = rs.getInt("id");
                item.text = rs.getString("text");
                item.isDone = rs.getBoolean("is_done");
                todoItems.add(item);
            }
        } catch (SQLException e) {}

        return todoItems.toArray(new TodoItem[0]);
    }

    public static TodoItem from (JsonObject jo) {
        TodoItem todoItem = new TodoItem();
        Object id = jo.getValue(new String[]{"id"});
        Object text = jo.getValue(new String[]{"text"});
        Object isDone = jo.getValue(new String[]{"isDone"});

        if (id instanceof Integer)
            todoItem.id = (int) jo.getValue(new String[]{"id"});
        if (text instanceof String)
            todoItem.text = (String) text;
        if (isDone instanceof Boolean)
            todoItem.isDone = (boolean) isDone;

        return todoItem;
    }

    public void validate() throws Exception {
        if (this.text.strip().length() == 0) {
            throw new Exception("Todo item must have text");
        }
    }

    public void validateId() throws Exception {
        if (this.id <= -1) {
            throw new Exception("The id is invalid");
        }
    }
}
