package ucf.assignments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Jeremy Rosales
 */
public class ToDoList extends Application {

    Stage window;
    Scene scene1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        window = stage;

        try {
            Parent root = FXMLLoader.load(getClass().getResource("ToDoList.fxml"));
            scene1 = new Scene(root);
            window.setScene(scene1);
            window.setTitle("To-Do List Maker");
            window.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
