package de.sebphil.todo.controller;

import de.sebphil.todo.entry.TodoEntry;
import de.sebphil.todo.entry.TodoParser;
import de.sebphil.todo.entry.TodoPriority;
import de.sebphil.todo.io.FileExplorer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox contentBox;

    private TodoParser parser;
    private List<TodoEntry> entries;
    private File rootFile;
    private boolean working;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        working = false;
        parser = new TodoParser();
        scrollPane.widthProperty().addListener(l -> contentBox.setPrefWidth(scrollPane.getWidth()));
    }

    @FXML
    void browseFile(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        Stage stage = new Stage();
        rootFile = chooser.showOpenDialog(stage);
        parseRootFile();
    }

    @FXML
    void browseDirectory(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        Stage stage = new Stage();
        rootFile = chooser.showDialog(stage);
        parseRootFile();
    }

    @FXML
    void refreshItems(ActionEvent event) {
        parseRootFile();
    }

    private void parseRootFile() {
        if(rootFile != null && rootFile.exists()) {
            parser.parseFiles(FileExplorer.getFiles(rootFile));
            setItems();
        }
    }

    private void setItems() {

        if(working)
            return;
        else
            working = true;

        entries = parser.getEntries();
        contentBox.getChildren().clear();

        addItems();
    }

    private void addItems() {
        Thread thread = new Thread(() -> {

            for(int i = 0; i < entries.size(); i++) {
                if(i == 500)
                    break;
                addItem(entries.get(i));
            }
            working = false;

        });
        thread.start();
    }

    private void addItem(TodoEntry entry) {
        TitledPane item = loadEntryItem(entry);
        item.setPrefWidth(scrollPane.getWidth());

        Platform.runLater(() -> contentBox.getChildren().add(item));

        scrollPane.widthProperty().addListener(l -> item.setPrefWidth(scrollPane.getWidth()));
    }

    private TitledPane loadEntryItem(TodoEntry entry) {
        FXMLLoader itemLoader = new FXMLLoader(getClass().getResource("/de/sebphil/todo/nodes/EntryItem.fxml"));
        TitledPane item = loadTitledPane(itemLoader);
        item.setExpanded(false);
        item.setText(entry.title);

        EntryItemController controller = itemLoader.getController();
        controller.setEntry(entry);
        setItemPriority(entry.priority, item);

        return item;
    }

    private TitledPane loadTitledPane(FXMLLoader itemLoader) {
        TitledPane pane = new TitledPane();
        try {
            pane = itemLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pane;
    }

    private void setItemPriority(TodoPriority priority, TitledPane item) {
        switch (priority) {
            case LOW -> item.setId("itemLow");
            case MEDIUM -> item.setId("itemMedium");
            case HIGH -> item.setId("itemHigh");
            case CRITICAL -> item.setId("itemCrit");
        }
    }
}
