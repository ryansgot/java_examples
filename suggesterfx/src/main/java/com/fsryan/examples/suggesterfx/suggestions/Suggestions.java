package com.fsryan.examples.suggesterfx.suggestions;

import java.util.List;
import com.fsryan.examples.suggestion.Suggester;

/*package*/ interface Suggestions {
    interface View {
        void showSuggestions(List<String> suggestions);
        void showNoSuggestions();
        void clearSuggestions();
    }

    interface Presenter {
        void onEntryTextFocus(String currentText);
        void onEntryTextFocusLost();
        void onEntryTextChanged(String currentText);
        void onTextSelected();
    }

    interface Model {
        Suggester getSuggester();
    }
}
