package com.fsryan.examples.suggesterfx.suggestions;

import com.fsryan.examples.suggestion.Suggester;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/*package*/ class PresenterImpl implements Suggestions.Presenter {

    private static final long FUTURE_TIMEOUT_MILLIS = 2500L;

    private Future<List<String>> currentFuture;

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private final Suggestions.View view;
    private volatile Suggester suggester;

    public PresenterImpl(Suggestions.View view, Suggestions.Model model) {
        this.view = view;
        model.loadSuggester(new Suggestions.Model.OnSuggesterLoadedListener() {
            @Override
            public void onSuccess(Suggester suggester) {
                setSuggester(suggester);
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

    @Override
    public void cleanUp() {
        executor.shutdownNow();
    }

    private void handleTextChange(String currentText) {
        if (currentText == null || currentText.length() == 0) {
            view.showNoSuggestions();
            return;
        }

        setCurrentRequest(new SuggestionRetriever(currentText));
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

    private void setCurrentRequest(Callable<List<String>> callable) {
        if (currentFuture != null && !currentFuture.isDone()) {
            currentFuture.cancel(true);
        }
        currentFuture = executor.submit(callable);
        List<String> suggestions = null;
        try {
            suggestions = currentFuture.get(FUTURE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (suggestions == null || suggestions.isEmpty()) {
            view.showNoSuggestions();
        } else {
            view.showSuggestions(suggestions);
        }
    }

    private class SuggestionRetriever implements Callable<List<String>> {

        private final String prefix;

        public SuggestionRetriever(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public List<String> call() throws Exception {
            if (getSuggester() == null) {
                return Collections.emptyList();
            }

            return getSuggester().suggest(prefix);
        }
    }
}
