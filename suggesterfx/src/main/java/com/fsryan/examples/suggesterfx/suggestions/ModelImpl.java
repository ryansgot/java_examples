package com.fsryan.examples.suggesterfx.suggestions;

import com.fsryan.examples.suggestion.Suggester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*package*/ class ModelImpl implements Suggestions.Model {

    @Override
    public void loadSuggester(final OnSuggesterLoadedListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Suggester suggester = new Suggester();
                BufferedReader reader = null;
                try {
                    File file = new File(getClass().getClassLoader().getResource("all_words.txt").getFile());
                    reader = new BufferedReader(new FileReader(file));
                    String line = null;
                    while (null != (line = reader.readLine())) {
                        line = line.trim();
                        if (line.isEmpty()) {
                            continue;
                        }
                        suggester.addWordToSuggestions(line);
                    }
                    listener.onSuccess(suggester);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    listener.onFailure(ioe);
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
