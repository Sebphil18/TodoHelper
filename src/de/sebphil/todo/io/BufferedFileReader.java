package de.sebphil.todo.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BufferedFileReader {

    private BufferedReader bufferedReader;
    private FileReader fileReader;
    private long lineNum;
    private final String filePath;

    public BufferedFileReader(File file) {
        lineNum = 0;
        filePath = file.getPath();
        try {
            this.fileReader = new FileReader(file);
            this.bufferedReader = new BufferedReader(fileReader);
        } catch (IOException e) {
            System.err.printf("ERROR::BufferedFileReader::Could not initialize reader for '%s'!%n", file.getPath());
            e.printStackTrace();
        }
    }

    // TODO [2020-11-18]: replace NULL as return-value with something better (bad practice)!
    public String nextLine() {
        lineNum++;
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Destructor
    public void close() {
        try {
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getLineNum() {
        return lineNum;
    }

    public String getFilePath() {
        return filePath;
    }

}
