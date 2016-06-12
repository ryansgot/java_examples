package com.fsryan.examples.suggesterfx.suggestions;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.List;

public class ViewContainer implements Suggestions.View {

    private static final int MAX_SUGGESTION_ROWS = 10;

    private final Suggestions.Presenter presenter;
    private final Scene scene;

    private ComboBox<String> suggestionComboBox;

    public ViewContainer() {
        scene = initScene();
        presenter = new PresenterImpl(this, new ModelImpl());
    }

    @Override
    public void showSuggestions(final List<String> suggestions) {
        // workaround for the fact that setVisibleRowCount does not invalidate view
        final int previousRows = Math.min(suggestionComboBox.getVisibleRowCount(), MAX_SUGGESTION_ROWS);
        final int newRows = Math.min(suggestions.size(), MAX_SUGGESTION_ROWS);
        if (previousRows != newRows)  {
            suggestionComboBox.hide();
            suggestionComboBox.setVisibleRowCount(newRows);
        }

        suggestionComboBox.getItems().clear();
        suggestionComboBox.getItems().addAll(suggestions);
        suggestionComboBox.show();
    }

    @Override
    public void showNoSuggestions() {
        suggestionComboBox.hide();
    }

    @Override
    public void showError(String message) {
        System.out.println("showError(" + message + ")");
    }

    @Override
    public void clearSuggestions() {
        suggestionComboBox.hide();
    }

    public Scene getScene() {
        return scene;
    }

    private Scene initScene() {
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new Insets(25, 25, 25, 25));

        Text instructions = new Text("Start typing for suggestions");
        instructions.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));
        layout.add(instructions, 0, 0, 2, 1);

        suggestionComboBox = new ComboBox<>();
        suggestionComboBox.setEditable(true);
        suggestionComboBox.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                // filter out navigation key events and selection key event
                switch (event.getCode()) {
                    case ENTER:
                        presenter.onTextSelected();
                    case END:
                    case HOME:
                    case PAGE_DOWN:
                    case PAGE_UP:
                    case LEFT:
                    case RIGHT:
                    case UP:
                    case DOWN:
                        return;
                }
                presenter.onEntryTextChanged(suggestionComboBox.getEditor().getText());
            }
        });

        layout.add(suggestionComboBox, 1, 1);

        return new Scene(layout, 300D, 250D);
    }

    public void onApplicationStop() {
        presenter.cleanUp();
    }
}
