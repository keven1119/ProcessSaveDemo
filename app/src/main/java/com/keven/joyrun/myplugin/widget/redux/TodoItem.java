package com.keven.joyrun.myplugin.widget.redux;

/**
 * Created by keven on 16/10/10.
 */

public class TodoItem {
    private long id;
    private String text;
    private boolean isChecked;

    public TodoItem(long id, String text, boolean isChecked) {
        this.id = id;
        this.text = text;
        this.isChecked = isChecked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
