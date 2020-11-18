package de.sebphil.todo.exception;

// TODO [2020-11-18]: misleading name (PriorityParseException?)
public class TodoParseException extends Exception{

    public TodoParseException(String parsedStr) {
        super(parsedStr);
        System.err.println("Error::TodoPriority::Could not parse priority '" + parsedStr + "'!");
    }

}
