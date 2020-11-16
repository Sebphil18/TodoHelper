package de.sebphil.todo.entry;

import de.sebphil.todo.exception.TodoParseException;

public enum TodoPriority {
    LOW, MEDIUM, HIGH, CRITICAL;

    public static TodoPriority parseStr(String priorityStr) {
        String formattedStr = priorityStr.toUpperCase();
        try {
            return switch (formattedStr) {
                case "LOW" -> TodoPriority.LOW;
                case "MEDIUM" -> TodoPriority.MEDIUM;
                case "HIGH" -> TodoPriority.HIGH;
                case "CRITICAL" -> TodoPriority.CRITICAL;
                default -> throw new TodoParseException(priorityStr);
            };
        } catch(TodoParseException e) {
            e.printStackTrace();
        }
        return TodoPriority.LOW;
    }

}
