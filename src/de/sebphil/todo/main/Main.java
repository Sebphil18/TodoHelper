package de.sebphil.todo.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/sebphil/todo/nodes/MainWindow.fxml"));
        BorderPane root = loader.load();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("TODO-Helper");
        stage.show();

    }
}
