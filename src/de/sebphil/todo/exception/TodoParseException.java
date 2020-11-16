package de.sebphil.todo.exception;

public class TodoParseException extends Exception{

    public TodoParseException(String parsedStr) {
        super(parsedStr);
        System.err.println("Error::TodoPriority::Could not parse priority '" + parsedStr + "'!");
    }

}
