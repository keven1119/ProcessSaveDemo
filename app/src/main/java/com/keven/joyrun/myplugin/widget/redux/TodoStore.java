package com.keven.joyrun.myplugin.widget.redux;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keven on 16/10/10.
 */

public class TodoStore {
    private List<TodoItem> todoItems = new ArrayList<>();

    public List<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void setTodoItems(List<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }
}
