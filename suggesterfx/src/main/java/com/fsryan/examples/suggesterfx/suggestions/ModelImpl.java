package com.fsryan.examples.suggesterfx.suggestions;

import com.fsryan.examples.suggestion.Suggester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*package*/ class ModelImpl implements Suggestions.Model {

    @Override
    public Suggester getSuggester() {
        Suggester ret = new Suggester();
        BufferedReader reader = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("all_words.txt").getFile());
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while (null != (line = reader.readLine())) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                ret.addWordToSuggestions(line);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }
}
