package de.sebphil.todo.text;

public class Line {

    private final String text;
    private final boolean containsTodo ;
    private final boolean isSectionStart;
    private final boolean isSectionEnd;
    private final boolean isComment;
    private final long lineNum;

    public Line(String text) {
        String upperLine = text.toUpperCase();
        this.text = text;
        containsTodo = upperLine.contains("TODO");
        isSectionStart = upperLine.contains("/*");
        isSectionEnd = upperLine.contains("*/");
        isComment = upperLine.contains("//");
        lineNum = 0;
    }

    public Line(String text, long lineNum) {
        String upperLine = text.toUpperCase();
        this.text = text;
        containsTodo = upperLine.contains("TODO");
        isSectionStart = upperLine.contains("/*");
        isSectionEnd = upperLine.contains("*/");
        isComment = upperLine.contains("//");
        this.lineNum = lineNum;
    }

    public boolean isSectionStart(){
        return isSectionStart;
    }

    public boolean isSectionEnd() {
        return isSectionEnd;
    }

    public boolean isComment() {
        return isComment;
    }

    public boolean containsTodo() {
        return containsTodo;
    }

    public String getText() {
        return text;
    }

    public long getLineNum() {
        return lineNum;
    }

}
