package de.sebphil.todo.text;

public class Line {

    private final String text;
    private boolean containsTodo ;
    private boolean isSectionStart;
    private boolean isSectionEnd;
    private boolean isComment;
    private final long lineNum;

    public Line(String text) {
        this.lineNum = 0;
        this.text = text;
        String upperText = text.toUpperCase();
        analyzeStr(upperText);
    }

    public Line(String text, long lineNum) {
        this.lineNum = lineNum;
        this.text = text;
        String upperText = text.toUpperCase();
        analyzeStr(upperText);
    }

    private void analyzeStr(String upperText) {
        containsTodo = upperText.contains("TODO");
        isSectionStart = upperText.contains("/*");
        isSectionEnd = upperText.contains("*/");
        isComment = upperText.contains("//");
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
