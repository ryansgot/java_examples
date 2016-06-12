package com.fsryan.examples.suggesterfx.suggestions;

import java.util.List;
import com.fsryan.examples.suggestion.Suggester;

/*package*/ interface Suggestions {
    interface View {
        void showSuggestions(List<String> suggestions);
        void showNoSuggestions();
        void showError(String message);
        void clearSuggestions();
    }

    interface Presenter {
        void onEntryTextFocus(String currentText);
        void onEntryTextFocusLost();
        void onEntryTextChanged(String currentText);
        void onTextSelected();

        void cleanUp();
    }

    interface Model {

        interface OnSuggesterLoadedListener {
            void onSuccess(Suggester suggester);
            void onFailure(Throwable t);

            OnSuggesterLoadedListener NOOP = new OnSuggesterLoadedListener() {
                public void onSuccess(Suggester suggester) {}
                public void onFailure(Throwable t) {}
            };
        }

        void loadSuggester(OnSuggesterLoadedListener listener);
    }
}
