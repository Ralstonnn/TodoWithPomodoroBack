package test.v1.todo.controllers;
import test.v1.todo.models.TodoItem;
import test.services.database.MariaDb;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TodoController {
    public static TodoItem[] getAll() throws SQLException {
        ResultSet rs = MariaDb.query("SELECT * FROM todo_item");
        return TodoItem.from(rs);
    }

    public static void insertOne(TodoItem todoItem) throws SQLException {
        todoItem.validate();
        String text = "\"" + todoItem.text + "\"";
        boolean isDone = todoItem.isDone;
        String query = "INSERT INTO todo_item (text, is_done) VALUE (%s, %b)".formatted(text, isDone);
        MariaDb.query(query);
    }
}
