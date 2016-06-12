package com.fsryan.examples.suggesterfx.suggestions;

import com.fsryan.examples.suggestion.Suggester;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/*package*/ class PresenterImpl implements Suggestions.Presenter {

    private static final int REQUEST_CAPACITY = 1;

    private final ArrayBlockingQueue<Request> requests = new ArrayBlockingQueue<>(REQUEST_CAPACITY);

    private final Suggestions.View view;
    private Suggester suggester;

    public PresenterImpl(Suggestions.View view, Suggestions.Model model) {
        this.view = view;
        model.loadSuggester(new Suggestions.Model.OnSuggesterLoadedListener() {
            @Override
            public void onSuccess(Suggester suggester) {
                setSuggester(suggester);
                while (!requests.isEmpty()) {   // <-- when suggester is loaded, perform the enqueued requests
                    requests.remove().perform();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                PresenterImpl.this.view.showError(t.getMessage());
                setSuggester(new Suggester());
            }
        });
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
        List<String> suggestions = getSuggestions(currentText, true);
        if (suggestions == null || suggestions.isEmpty()) {
            view.showNoSuggestions();
        } else {
            view.showSuggestions(suggestions);
        }
    }

    private List<String> getSuggestions(final String prefix, boolean enqueueIfNotLoaded) {
        if (prefix == null || prefix.length() == 0) {
            return Collections.emptyList();
        }
        Suggester s = getSuggester();
        if (s == null) {
            if (enqueueIfNotLoaded) {
                enqueueRequest(new Request() {
                    @Override
                    public void perform() {
                        getSuggestions(prefix, false);
                    }
                });
            }
            return Collections.emptyList();
        } else {
            return s.suggest(prefix);
        }
    }

    private interface Request {
        void perform();
    }

    private void enqueueRequest(Request request) {
        while (requests.size() >= REQUEST_CAPACITY) {
            requests.remove();
        }
        requests.add(request);
    }

    private void setSuggester(Suggester suggester) {
        synchronized (this) {
            this.suggester = suggester;
        }
    }

    private Suggester getSuggester() {
        synchronized (this) {
            return suggester;
        }
    }
}
