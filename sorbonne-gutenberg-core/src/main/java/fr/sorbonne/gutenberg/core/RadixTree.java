package fr.sorbonne.gutenberg.core;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class RadixTree {
    private Node root;
    private InputStream is;

    public RadixTree(Index index) {
        root = new Node();
        add(index);
    }

    public RadixTree(InputStream is) {
        this.is = is;
        readIndex(is);
    }

    public List<Word> search(String value) {
        return root.search(value);
    }

    private void add(Index index) {
        var records = index.getWords()
                .values()
                .stream()
                .collect(groupingBy(word -> word.getWord().charAt(0)));
        add(records);
    }

    private void add(Map<Character, List<Word>> records) {
        root = new Node();
        root.setCurrent('\0');
        root.setWord(Word.EMPTY);
        root.addChildren(records, 0);
    }

    private void readIndex(InputStream is) {

        var records = new BufferedReader(new InputStreamReader(is))
                .lines()
                .parallel()
                .map(Word::parse)
                .collect(groupingBy(word -> word.getWord().charAt(0)));
        add(records);
    }
}
