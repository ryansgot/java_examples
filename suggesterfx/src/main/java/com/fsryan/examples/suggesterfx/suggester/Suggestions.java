package com.fsryan.examples.suggesterfx.suggester;

import java.util.List;
import com.fsryan.examples.suggestion.Suggester;

public interface Suggestions {
    interface View {
        void showSuggestions(List<String> suggestions);
        void showNoSuggestions();
        void clearSuggestions();
    }

    interface Presenter {
        void onEntryTextFocus(String currentText);
        void onEntryTextChanged(String currentText);
    }

    interface Model {
        Suggester getSuggester();
    }
}
