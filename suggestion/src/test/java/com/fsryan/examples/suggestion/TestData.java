package com.fsryan.examples.suggestion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestData {

    private static final String RESOURCE_DIR = "src" + File.separator + "test" + File.separator + "resources";

    private static List<String> allWords;

    public static List<String> allWords() throws Exception {
        if (allWords == null) {
            allWords = wordsFromResource("suggestion/all_words.txt");
        }
        return allWords;
    }

    public static List<String> allWordsMatchingPrefix(String prefix) throws Exception {
        if (prefix == null) {
            return allWords();
        }
        List<String> matchingWords = new ArrayList<>();
        for (String word : allWords()) {
            if (word.startsWith(prefix)) {
                matchingWords.add(word);
            }
        }
        return matchingWords;
    }

    /**
     * <p>
     *     Reads a resource file line-by-line assuming one word per line. Output is a list containing each line
     * </p>
     * @param resource filename of the resource from which to read
     * @return a List&gt;String&lt; containing the lines of the file
     */
    public static List<String> wordsFromResource(String resource) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(RESOURCE_DIR + File.separator + resource));
        String line;
        List<String> ret = new ArrayList<>();
        while (null != (line = reader.readLine())) {
            line = line.trim();
            if (!line.isEmpty()) {
                ret.add(line.trim());
            }
        }
        reader.close();
        return ret;
    }
}
