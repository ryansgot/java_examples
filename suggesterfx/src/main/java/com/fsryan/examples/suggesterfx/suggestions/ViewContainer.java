package com.fsryan.examples.suggesterfx.suggestions;


import com.fsryan.examples.suggestion.Suggester;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
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
        System.out.println("showSuggestions(" + suggestions.toString() + ")");
        suggestionComboBox.getItems().clear();

        // workaround for the fact that setVisibleRowCount does not invalidate view
        if (suggestions.size() < MAX_SUGGESTION_ROWS)  {
            suggestionComboBox.hide();
            suggestionComboBox.setVisibleRowCount(suggestions.size());
        }

        suggestionComboBox.getItems().addAll(suggestions);
        suggestionComboBox.show();
    }

    @Override
    public void showNoSuggestions() {
        System.out.println("showNoSuggestions()");
        suggestionComboBox.hide();
    }

    @Override
    public void clearSuggestions() {
        System.out.println("clearSuggestions()");
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
}
