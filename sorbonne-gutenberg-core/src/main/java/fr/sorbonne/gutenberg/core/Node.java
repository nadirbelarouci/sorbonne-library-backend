package fr.sorbonne.gutenberg.core;


import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class Node {
    private Word word;
    private char current;
    private Map<Character, Node> children = new HashMap<>();

    public Node() {
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public void setCurrent(char current) {
        this.current = current;
    }

    public void addChildren(Map<Character, List<Word>> records, int length) {
        records.forEach((c, words) -> children.put(c, createNode(c, words, length + 1)));
    }

    public Node createNode(char c, List<Word> words, int length) {
        Node node = new Node();
        node.setCurrent(c);
        if (words.size() == 1) {
            node.setWord(words.get(0));
        } else {
            Map<Character, List<Word>> children = words.stream()
                    .filter(word -> node.isValid(word, length))
                    .collect(groupingBy(word -> word.getWord().charAt(length)));
            node.addChildren(children, length);
        }
        return node;
    }

    private boolean isValid(Word word, int length) {
        if (word.getWord().length() == length) {
            setWord(word);
            return false;
        }
        return true;
    }

    public List<Word> search(String value) {
        Node current = this;
        for (int i = 0; i < value.length() && current != null; i++) {
            current = current.children.get(value.charAt(i));
        }
        if (current != null)
            return current.collect();
        return Collections.emptyList();
    }

    private List<Word> collect() {
        List<Word> list = new ArrayList<>();
        if (word != null)
            list.add(word);

        list.addAll(children
                .values()
                .stream()
                .flatMap(node -> node.collect().stream())
                .collect(toList()));
        return list;
    }

    @Override
    public String toString() {
        return current + "";
    }
}
