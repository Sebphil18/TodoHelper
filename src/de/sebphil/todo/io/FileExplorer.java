package de.sebphil.todo.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileExplorer {

    public static List<File> getFiles(File rootFile) {
        List<File> files = new ArrayList<>();
        exploreFile(rootFile, files);
        return files;
    }

    private static void exploreFile(File rootFile, List<File> files) {
        if(rootFile.isDirectory()) {

            String[] entries = rootFile.list();
            if(entries != null) {
                for(String entry : entries) {
                    File entryFile = new File(rootFile.getPath() + "\\" + entry);
                    exploreFile(entryFile, files);
                }
            }

        } else {
            files.add(rootFile);
        }
    }

}
