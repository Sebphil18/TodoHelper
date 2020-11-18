package de.sebphil.todo.entry;

import de.sebphil.todo.io.BufferedFileReader;
import de.sebphil.todo.text.CommentSection;
import de.sebphil.todo.text.Line;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TodoParser {

    private final List<TodoEntry> entries;
    private long totalLines;

    public TodoParser() {
        entries = new ArrayList<>();
    }

    public void parseFiles(List<File> files) {
        entries.clear();
        totalLines = 0;
        for(File file : files)
            parseFile(file);
        System.out.println("processed " + totalLines + " lines");
    }

    private void parseFile(File file) {
        BufferedFileReader fileReader = new BufferedFileReader(file);
        readFile(fileReader);
        fileReader.close();
    }

    private void readFile(BufferedFileReader fileReader) {
        String text;
        CommentSection section = new CommentSection();

        while((text = fileReader.nextLine()) != null) {
            Line line = new Line(text, fileReader.getLineNum());
            parseLine(line, section, fileReader.getFilePath());
        }
        totalLines += fileReader.getLineNum() - 1;
    }

    private void parseLine(Line line, CommentSection section, String filePath) {
        if(line.isSectionEnd()) {
            closeSection(section, filePath, line.getLineNum());
        } else if(line.isSectionStart() || section.isOpen()) {
            section.addLine(line);
        } else if(line.containsTodo() && line.isComment()) {
            parseString(line.getText(), filePath, line.getLineNum());
        }
    }

    private void closeSection(CommentSection section, String filePath, long lineNum) {
        if(section.containsTodo()) {
            String sectionText = section.getComment();
            parseString(sectionText, filePath, lineNum - section.size());
        }
        section.clear();
    }

    private void parseString(String str, String filePath, long lineNum) {

        String todoText = getTodoText(str);
        if(todoText.isEmpty())
            return;

        String[] lines = todoText.split("\n");
        String header = lines[0];

        String date = getHeaderDate(header);
        TodoPriority priority = getHeaderPriority(header);
        String title = getHeaderTitle(header, date, priority.toString());
        String msg = getTodoMsg(lines);

        addEntry(title, msg, date, filePath, lineNum, priority);
    }

    private String getTodoText(String str) {
        String upperStr = str.toUpperCase();
        int todoIndex = upperStr.indexOf("TODO");
        if(todoIndex < 0)
            return "";
        return str.substring(todoIndex);
    }

    private String getHeaderDate(String header) {
        String date = "";
        int dateBegin = header.indexOf("[");
        int dateEnd = header.indexOf("]");
        if(dateEnd - dateBegin > 0)
            date = header.substring(dateBegin + 1, dateEnd);
        return date;
    }

    private TodoPriority getHeaderPriority(String header) {
        int priBegin = header.indexOf("(");
        int priEnd = header.indexOf(")");
        if(priEnd - priBegin > 0) {
            String priorityStr = header.substring(priBegin + 1, priEnd).toUpperCase();
            return TodoPriority.parseStr(priorityStr);
        }
        return TodoPriority.LOW;
    }

    private String getHeaderTitle(String header, String date, String priority) {
        header = header.replace("["+date+"]", "");
        header = header.replace("("+ priority +")", "");
        header = header.replace("("+ priority.toLowerCase()+")", "");
        return header;
    }

    private String getTodoMsg(String[] lines) {
        StringBuilder msgBuilder = new StringBuilder();

        for(int i = 1; i < lines.length; i++) {
            String line = lines[i];

            if(line.startsWith("*") && line.length() > 2)
                line = line.substring(line.indexOf("*") + 2);

            msgBuilder.append(line).append("\n");
        }

        return msgBuilder.toString();
    }

    // TODO [2020-11-18]: too many arguments!
    private void addEntry(String title, String msg, String date, String filePath, long lineNum, TodoPriority priority) {
        TodoEntry entry = new TodoEntry(title, msg, filePath, lineNum);
        entry.priority = priority;
        setEntryDate(date, filePath, lineNum, entry);
        entries.add(entry);
    }

    private void setEntryDate(String date, String filePath, long lineNum, TodoEntry entry) {
        if(!date.isEmpty()) {
            try {
                entry.date = LocalDate.parse(date);
            } catch (DateTimeParseException e) {
                System.err.println("Warning::Could not parse date: " + date
                        + " in file " + filePath + " line " + lineNum);
            }
        }
    }

    public List<TodoEntry> getEntries() {
        return entries;
    }

}
