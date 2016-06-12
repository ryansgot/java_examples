package com.fsryan.examples.suggesterfx.suggestions;

import com.fsryan.examples.suggestion.Suggester;

import java.util.Collections;
import java.util.List;

/*package*/ class PresenterImpl implements Suggestions.Presenter {

    private final Suggestions.View view;
    private Suggester suggester;

    public PresenterImpl(Suggestions.View view, Suggestions.Model model) {
        this.view = view;
        suggester = model == null ? new Suggester() : model.getSuggester();
        suggester = suggester == null ? new Suggester() : suggester;
    }

    @Override
    public void onEntryTextFocus(String currentText) {
        handleTextChange(currentText);
    }

    @Override
    public void onEntryTextFocusLost() {
        view.clearSuggestions();
    }

    @Override
    public void onEntryTextChanged(String currentText) {
        handleTextChange(currentText);
    }

    @Override
    public void onTextSelected() {
        view.clearSuggestions();
    }

    private void handleTextChange(String currentText) {
        List<String> suggestions = getSuggestions(currentText);
        if (suggestions == null || suggestions.isEmpty()) {
            view.showNoSuggestions();
        } else {
            view.showSuggestions(suggestions);
        }
    }

    private List<String> getSuggestions(String prefix) {
        return prefix == null || prefix.length() == 0 ? Collections.<String>emptyList() : suggester.suggest(prefix);
    }
}
