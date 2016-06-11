package com.fsryan.examples.suggesterfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Suggestions FX");


        StackPane layout = new StackPane();
        Scene scene = new Scene(layout, 300D, 200D);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
