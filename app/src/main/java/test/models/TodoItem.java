package test.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TodoItem {
    public int id;
    public String text;
    public boolean isDone;

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
}
