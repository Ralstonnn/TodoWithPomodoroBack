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
    public static void insertOne(TodoItem todoItem) throws Exception {
        todoItem.validate();
        String text = "\"" + todoItem.text + "\"";
        boolean isDone = todoItem.isDone;
        String query = "INSERT INTO todo_item (text, is_done) VALUE (%s, %b)".formatted(text, isDone);
        MariaDb.query(query);
    }
    public static void changeOne(TodoItem todoItem) throws Exception {
        todoItem.validateId();
        int id = todoItem.id;
        String text = "\"" + todoItem.text + "\"";
        boolean isDone = todoItem.isDone;
        String query = "UPDATE todo_item AS ti SET ti.`text` = %s, ti.is_done = %b WHERE ti.id = %s ".formatted(text, isDone, id);
        MariaDb.query(query);
    }
    public static void deleteOne(TodoItem todoItem) throws Exception {
        todoItem.validateId();
        int id = todoItem.id;
        String query = "DELETE FROM todo_item WHERE id=%s ".formatted(id);
        MariaDb.query(query);
    }
    public static boolean setIsDone(TodoItem todoItem) throws Exception {
        todoItem.validateId();
        int id = todoItem.id;
        boolean isDone = todoItem.isDone;
        String query = "UPDATE todo_item AS ti SET ti.is_done = %b WHERE ti.id = %s ".formatted(isDone, id);
        MariaDb.query(query);
        return isDone;
    }
}
