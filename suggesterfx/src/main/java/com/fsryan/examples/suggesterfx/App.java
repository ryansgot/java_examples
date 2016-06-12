package com.fsryan.examples.suggesterfx;

import com.fsryan.examples.suggesterfx.suggestions.ViewContainer;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    private ViewContainer viewContainer;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Suggestions FX");

        viewContainer = new ViewContainer();
        primaryStage.setScene(viewContainer.getScene());
        primaryStage.show();
    }

    @Override
    public void stop() {
        viewContainer.onApplicationStop();
    }
}
