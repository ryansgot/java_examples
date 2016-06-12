package com.fsryan.examples.suggesterfx.suggestions;

import com.fsryan.examples.suggestion.Suggester;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public abstract class PresenterImplTest {

    private Suggestions.Presenter presenterUnderTest;

    private Suggestions.View mockView;
    private Suggestions.Model mockModel;

    @Before
    public void setUp() {
        mockView = mock(Suggestions.View.class);

        // set up the model to return the mockSuggester, which will return the wordsToSuggest
        mockModel = mock(Suggestions.Model.class);
        Suggester mockSuggester = mock(Suggester.class);
        when(mockSuggester.suggest(any(String.class))).thenReturn(wordsToSuggest());
        when (mockModel.getSuggester()).thenReturn(mockSuggester);

        presenterUnderTest = new PresenterImpl(mockView, mockModel);
    }

    protected abstract List<String> wordsToSuggest();

    protected Suggestions.Presenter presenterUnderTest() {
        return presenterUnderTest;
    }

    protected Suggestions.View mockView() {
        return mockView;
    }

    protected Suggestions.Model mockModel() {
        return mockModel;
    }

    @RunWith(Parameterized.class)
    public static class EntryTextFocused extends PresenterImplTest {

        private final String inputPrefix;
        private final List<String> wordsToSuggest;
        private final int showSuggestionsTimes;
        private final int showNoSuggestionsTimes;

        public EntryTextFocused(String inputPrefix, List<String> wordsToSuggest, int showSuggestionsTimes, int showNoSuggestionsTimes) {
            this.inputPrefix = inputPrefix;
            this.wordsToSuggest = wordsToSuggest;
            this.showSuggestionsTimes = showSuggestionsTimes;
            this.showNoSuggestionsTimes = showNoSuggestionsTimes;
        }

        @Parameterized.Parameters
        public static Iterable<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    // 00: empty prefix
                    {
                            "",                     // input prefix
                            Lists.newArrayList(),   // Words to suggest
                            0,                      // times to call showSuggestions
                            1                       // times to call showNoSuggestions
                    },
                    // 01: null prefix
                    {
                            null,                   // input prefix
                            Lists.newArrayList(),   // Words to suggest
                            0,                      // times to call showSuggestions
                            1                       // times to call showNoSuggestions
                    },
                    // 02: nonempty, nonnull prefix with no words to suggest
                    {
                            "a",                    // input prefix
                            Lists.newArrayList(),   // Words to suggest
                            0,                      // times to call showSuggestions
                            1                       // times to call showNoSuggestions
                    },
                    // 03: somehow, wordsToSuggest is nul
                    {
                            "a",                    // input prefix
                            null,                   // Words to suggest
                            0,                      // times to call showSuggestions
                            1                       // times to call showNoSuggestions
                    },
                    // 04: nonempty, nonnull prefix with some words to suggest
                    {
                            "a",                            // input prefix
                            Lists.newArrayList("alo"),      // Words to suggest
                            1,                              // times to call showSuggestions
                            0                               // times to call showNoSuggestions
                    },
            });
        }

        @Test
        public void shouldCallShowSuggestionsCorrectNumberOfTimesOnEntryTextFocusWithAnyList() {
            presenterUnderTest().onEntryTextFocus(inputPrefix);
            verify(mockView(), times(showSuggestionsTimes)).showSuggestions(any(List.class));
        }

        @Test
        public void shouldCallShowSuggestionsCorrectNumberOfTimesOnEntryTextFocusWithExactList() {
            presenterUnderTest().onEntryTextFocus(inputPrefix);
            verify(mockView(), times(showSuggestionsTimes)).showSuggestions(eq(wordsToSuggest));
        }

        @Test
        public void shouldCallShowNoSuggestionsCorrectNumberOfTimesOnEntryTextFocus() {
            presenterUnderTest().onEntryTextFocus(inputPrefix);
            verify(mockView(), times(showNoSuggestionsTimes)).showNoSuggestions();
        }

        @Test
        public void shouldCallShowSuggestionsCorrectNumberOfTimesOnEntryTextChangedWithAnyList() {
            presenterUnderTest().onEntryTextChanged(inputPrefix);
            verify(mockView(), times(showSuggestionsTimes)).showSuggestions(any(List.class));
        }

        @Test
        public void shouldCallShowSuggestionsCorrectNumberOfTimesOnEntryTextChangedWithExactList() {
            presenterUnderTest().onEntryTextChanged(inputPrefix);
            verify(mockView(), times(showSuggestionsTimes)).showSuggestions(eq(wordsToSuggest));
        }

        @Test
        public void shouldCallShowNoSuggestionsCorrectNumberOfTimesOnEntryTextChanged() {
            presenterUnderTest().onEntryTextChanged(inputPrefix);
            verify(mockView(), times(showNoSuggestionsTimes)).showNoSuggestions();
        }

        @Override
        protected List<String> wordsToSuggest() {
            return wordsToSuggest;
        }
    }

    public static class ClearSuggestions extends PresenterImplTest {

        @Test
        public void shouldCallClearSuggestionsOnFocusLost() {
            presenterUnderTest().onEntryTextFocusLost();
            verify(mockView(), times(1)).clearSuggestions();
        }

        @Test
        public void shouldCallClearSuggestionsOnTextSelected() {
            presenterUnderTest().onTextSelected();
            verify(mockView(), times(1)).clearSuggestions();
        }

        @Override
        protected List<String> wordsToSuggest() {
            return Collections.emptyList();
        }
    }
}
