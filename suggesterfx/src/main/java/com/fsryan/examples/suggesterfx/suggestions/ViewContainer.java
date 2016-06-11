package com.fsryan.examples.suggesterfx.suggestions;


import com.fsryan.examples.suggestion.Suggester;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.util.List;

public class ViewContainer implements Suggestions.View {

    private final Suggestions.Presenter presenter;
    private final Scene scene;

    public ViewContainer() {
        scene = initScene();
        // TODO: inject the model
        presenter = new PresenterImpl(this, new Suggestions.Model() {
            @Override
            public Suggester getSuggester() {
                return new Suggester();
            }
        });
    }

    @Override
    public void showSuggestions(List<String> suggestions) {

    }

    @Override
    public void showNoSuggestions() {

    }

    @Override
    public void clearSuggestions() {

    }

    public Scene getScene() {
        return scene;
    }

    private Scene initScene() {
        StackPane layout = new StackPane();
        return new Scene(layout, 300D, 200D);
    }
}
