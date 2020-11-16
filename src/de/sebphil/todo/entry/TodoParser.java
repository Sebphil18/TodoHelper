package de.sebphil.todo.entry;

import de.sebphil.todo.text.CommentSection;
import de.sebphil.todo.text.Line;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TodoParser {

    private final List<TodoEntry> entries;
    private static long totalLines;

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

    // TODO [2020-11-16](low): move file-reading into separate class!
    private void parseFile(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader buffReader = new BufferedReader(fileReader);
            readFile(file, buffReader);
            buffReader.close();
            fileReader.close();
        } catch (IOException e) {
            System.err.print("Could not parse file " + file.getAbsolutePath() + "! ");
            System.err.println("(The file may not exist or the user does not have access permissions)");
            e.printStackTrace();
        }
    }

    private void readFile(File file, BufferedReader reader) throws IOException {
        System.out.println("Processing file: " + file.getPath());

        String text;
        long lineNum = 0;

        CommentSection section = new CommentSection();

        while((text = reader.readLine()) != null) {
            lineNum++;
            totalLines++;
            Line line = new Line(text, lineNum);
            parseLine(line, section, file);
        }
    }

    private void parseLine(Line line, CommentSection section, File file) {
        if(line.isSectionEnd()) {
            closeSection(section, file, line.getLineNum());
        } else if(line.isSectionStart() || section.isOpen()) {
            addToSection(line.getText(), line, section);
        } else if(line.containsTodo() && line.isComment()) {
            parseString(line.getText(), file, line.getLineNum());
        }
    }

    private void closeSection(CommentSection section, File file, long lineNum) {
        if(section.containsTodo()) {
            String sectionText = section.getComment();
            parseString(sectionText, file, lineNum - section.size());
        }
        section.clear();
    }

    private void addToSection(String line, Line properties, CommentSection section) {
        if(properties.containsTodo())
            section.setContainsTodo(true);
        section.addLine(line);
    }

    private void parseString(String str, File file, long lineNum) {

        String todoText = getTodoText(str);
        if(todoText.isEmpty())
            return;

        String[] lines = todoText.split("\n");
        String header = lines[0];

        String date = getHeaderDate(header);
        TodoPriority priority = getHeaderPriority(header);
        String title = getHeaderTitle(header, date, priority.toString());
        String msg = getTodoMsg(lines);

        addEntry(title, msg, date, file.getPath(), lineNum, priority);
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
                System.err.println("Warning::Could not parse date: " + date + " in file " + filePath + " line " + lineNum);
            }
        }
    }

    public List<TodoEntry> getEntries() {
        return entries;
    }

}
