package de.sebphil.todo.controller;

import de.sebphil.todo.entry.TodoEntry;
import de.sebphil.todo.entry.TodoPriority;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EntryItemController implements Initializable {

    @FXML
    private Label messageLab;

    @FXML
    private Label messageText;

    @FXML
    private Label fileText;

    @FXML
    private Label lineText;

    @FXML
    private Label dateText;

    @FXML
    private Label priorityText;

    private TodoEntry entry;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    @FXML
    void openFilePath(MouseEvent event) {
        try {
            Desktop.getDesktop().open(new File(entry.filePath.substring(0, entry.filePath.lastIndexOf("\\"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setEntry(TodoEntry entry) {
        this.entry = entry;
        fileText.setText(entry.filePath);
        lineText.setText(Long.toString(entry.line));
        dateText.setText(entry.date.toString());
        setMessage();
        setPriority(entry.priority);
    }

    private void setMessage() {
        if(entry.msg.isEmpty()) {
            messageLab.setVisible(false);
            messageText.setVisible(false);
        }
        messageText.setText(entry.msg);
    }

    private void setPriority(TodoPriority priority) {
        priorityText.setText(priority.toString());
        setPriorityCol(priority);
    }

    private void setPriorityCol(TodoPriority priority) {
        switch (priority) {
            case LOW -> priorityText.setTextFill(Color.web("#8F9CFF"));
            case MEDIUM -> priorityText.setTextFill(Color.web("#75EF7B"));
            case HIGH -> priorityText.setTextFill(Color.web("#F0E600"));
            case CRITICAL -> priorityText.setTextFill(Color.web("#C13200"));
            default -> priorityText.setTextFill(Color.GRAY);
        }
    }

}
