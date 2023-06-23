package com.example.binarytreeclientserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Client Run class
 */
public class ClientRun extends Application {
    public static void  main(String[] args){
        launch();
    }


    @Override
    public void start(Stage stage) throws IOException {
        BorderPane root = new BorderPane();
        FXMLLoader fxmlLoader = new FXMLLoader(ClientRun.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);


        stage.setTitle("Client");
        stage.setScene(scene);
        stage.show();
        scene.getWindow().setOnCloseRequest(event -> System.exit(0));
    }
}
