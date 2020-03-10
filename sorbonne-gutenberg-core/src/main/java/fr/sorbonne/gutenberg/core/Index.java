package fr.sorbonne.gutenberg.core;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Index {
    private static Map<String, int[]> kmpTable = new ConcurrentHashMap<>();
    private Stream<String> lines;
    private String title = "";
    private String author = "";
    private int currentLine;
    private Map<String, Word> words = new ConcurrentHashMap<>();

    public Index(String fileName) throws IOException {
        this(Paths.get(fileName));
    }

    public Index(Path path) throws IOException {
        this.lines = Files.lines(path);
        process();
    }

    public Index(InputStream stream) throws IOException {
        this.lines = new BufferedReader(new InputStreamReader(stream)).lines();
        process();
    }


    private void process() throws IOException {
        this.lines
                .map(String::toLowerCase)
                .filter(line -> !line.isEmpty())
                .forEach(this::processLine);
    }

    private void processLine(String line) {
        int lineIndex = ++currentLine;
        var words = line.split("[^a-zA-Z]");
        resolveTitle(line);

        Stream.of(words)
                .parallel()
                .unordered()
                .distinct()
                .filter(word -> !word.isEmpty())
                .filter(word -> word.length() > 3)
                .forEach(word -> createWord(line, word, lineIndex));
    }

    private void createWord(String line, String word, int i) {
        int[] table = kmpTable.computeIfAbsent(word, pattern -> KMP.kmpTable(word));
        Word record = words.computeIfAbsent(word, pattern -> new Word(word));
        KMP.matchAll(word.toCharArray(), table, line.toCharArray()).forEach(column -> record.addPosition(i, column, line));
    }

    private void resolveTitle(String line) {
        if (title.isEmpty() && line.contains("title:")) {
            title = line.replace("title:", "").strip();
        } else if (author.isEmpty() && line.contains("author:")) {
            author = line.replace("author:", "").strip();
        }
    }


    public Map<String, Word> getWords() {
        return words;
    }

    public ByteArrayOutputStream save() {
        var os = new ByteArrayOutputStream();
        words.values()
                .stream()
                .map(Word::toString)
                .forEach(s -> write(os, s));
        return os;
    }

    private void write(ByteArrayOutputStream os, String value) {
        try {
            os.write(value.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}
