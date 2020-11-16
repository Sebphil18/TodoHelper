package de.sebphil.todo.text;

import java.util.ArrayList;
import java.util.List;

public class CommentSection {

    private final List<String> lines;
    private boolean containsTodo;
    private boolean open;

    public CommentSection() {
        this.lines = new ArrayList<>();
        containsTodo = false;
        open = false;
    }

    public String getComment() {
        StringBuilder builder = new StringBuilder();
        for(String line : lines)
            builder.append(line).append("\n");
        return builder.toString();
    }

    public void addLine(String line) {
        lines.add(line);
        if(!open)
            open = true;
    }

    public void clear() {
        lines.clear();
        containsTodo = false;
        open = false;
    }

    public boolean isOpen() {
        return open;
    }

    public void setContainsTodo(boolean b) {
        containsTodo = b;
    }

    public boolean containsTodo() {
        return containsTodo;
    }

    public int size() {
        return lines.size();
    }

}
