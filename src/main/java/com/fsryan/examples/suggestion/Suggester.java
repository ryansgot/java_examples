package com.fsryan.examples.suggestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Suggester implements Iterable<String> {

    private int size = 0;
    private final Node root = new Node((char) 0);

    public Suggester() {
        this(null);
    }

    public Suggester(List<String> initWords) {
        reinit(initWords);
    }

    public void reinit(List<String> initWords) {
        clear();
        for (String word : initWords == null ? new ArrayList<String>() : initWords) {
            addWordToSuggestions(word);
        }
    }

    /**
     * <p>
     *     Makes suggestions based upon the words that have been added to the suggester
     * </p>
     * @param prefix
     * @return A list of suggestions that start with the prefix entered
     */
    public List<String> suggest(String prefix) {
        if (prefix == null || prefix.length() == 0) {
            return Collections.emptyList();
        }

        Node current = root;
        StringBuffer buf = new StringBuffer();

        int idx = 0;
        while (idx < prefix.length()) {
            char c = prefix.charAt(idx);
            buf.append(c);
            current = current.getChild(c);
            if (current == null) {
                return Collections.emptyList();
            }
            idx++;
        }

        List<String> ret = new ArrayList<>();
        if (current.isSuggestion()) {
            ret.add(buf.toString());
        }
        ret.addAll(suggestInternal(current, buf));
        return ret;
    }

    public void clear() {
        root.getChildren().clear();
    }

    /**
     * @return the number of words that can be suggested
     */
    public int size() {
        return size;
    }

    /**
     * @return an iterator that iterates over each word that can be suggested in order
     */
    @Override
    public Iterator<String> iterator() {
        return suggestInternal(root, new StringBuffer()).iterator();
    }

    public void addWordToSuggestions(String word) {
        Node current = root;
        int idx = 0;
        while (idx < word.length()) {
            Node child = current.getOrCreateChild(word.charAt(idx));
            child.setSuggestion(child.isSuggestion() || idx == word.length() - 1);
            current = child;
            idx++;
        }
        size++;
    }

    private List<String> suggestInternal(Node current, StringBuffer buf) {
        List<String> ret = new ArrayList<>();
        for (Node child : current.getChildren()) {
            buf.append(child.getC());
            if (child.isSuggestion()) {
                ret.add(buf.toString());
            }
            ret.addAll(suggestInternal(child, buf));
            buf.delete(buf.length() - 1, buf.length());
        }
        return ret;
    }
}