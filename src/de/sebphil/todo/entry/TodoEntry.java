package de.sebphil.todo.entry;

import java.time.LocalDate;

public class TodoEntry {

    public long line;
    public String filePath;
    public String title;
    public String msg;
    public LocalDate date;
    public TodoPriority priority;

    public TodoEntry(String title, String filePath, long line) {
        this.title = title;
        this.msg = "";
        this.filePath = filePath;
        this.line = line;
        this.date = LocalDate.now();
        this.priority = TodoPriority.LOW;
    }

    public TodoEntry(String title, String msg, String filePath, long line) {
        this.title = title;
        this.msg = msg;
        this.filePath = filePath;
        this.line = line;
        this.date = LocalDate.now();
        this.priority = TodoPriority.LOW;
    }

}