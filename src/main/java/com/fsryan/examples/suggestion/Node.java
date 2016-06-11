package com.fsryan.examples.suggestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*package*/ class Node implements Comparable<Node> {

    private final char c;
    private final List<Node> children;
    private boolean suggestion;

    public Node(char c) {
        this(c, false);
    }

    public Node(char c, boolean suggestion) {
        this(c, suggestion, new ArrayList<Node>());
    }

    private Node(char c, boolean suggestion, List<Node> children) {
        this.c = Character.toLowerCase(c);
        this.suggestion = suggestion;
        this.children = children;
    }

    public char getC() {
        return c;
    }

    public List<Node> getChildren() {
        return children;
    }

    public boolean isSuggestion() {
        return suggestion;
    }

    public void setSuggestion(boolean suggestion) {
        this.suggestion = suggestion;
    }

    public Node getChild(char c) {
        for (Node child : children) {
            if (child.getC() == Character.toLowerCase(c)) {
                return child;
            }
        }
        return null;
    }

    public Node getOrCreateChild(char c) {
        Node n = getChild(c);
        if (n != null) {
            return n;
        }
        n = new Node(c);
        children.add(n);
        Collections.sort(children);
        return n;
    }

    @Override
    public int compareTo(Node o) {
        return (int) c - (int) o.getC();
    }
}
