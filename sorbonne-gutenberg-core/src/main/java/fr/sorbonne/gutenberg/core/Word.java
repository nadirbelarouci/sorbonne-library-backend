package fr.sorbonne.gutenberg.core;


import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Word {
    private static final Pattern IndexedWordPattern = Pattern.compile("(\\w+):\\d+:(.*)");
    private static final Pattern COMMA = Pattern.compile(",");
    public static Word EMPTY = new Word("");
    private String word;
    private Set<Position> positions = ConcurrentHashMap.newKeySet();

    public Word(String word) {
        this.word = word;
    }

    public static Word parse(String indexedWord) {
        Matcher matcher = IndexedWordPattern.matcher(indexedWord);
        Word word = new Word("");
        String positions = "";
        if (matcher.find()) {
            word.word = matcher.group(1);
            positions = matcher.group(2);
        }

        COMMA.splitAsStream(positions)
                .parallel()
                .map(Position::parse)
                .forEach(word.positions::add);
        return word;
    }


    public void addPosition(int line, int column, String row) {
        positions.add(new Position(line, column, row));
    }

    public String getWord() {
        return word;
    }


    @Override
    public String toString() {
        return word + ":" + positions.size() + ":" + positions.stream()
                .map(Position::toString)
                .collect(Collectors.joining(",")) + "\n";
    }

    private static class Position {
        private static final Pattern PositionPattern = Pattern.compile("(?:(\\d+)-(\\d+))");

        private int line;
        private int column;
        private String row;

        public Position(int line, int column, String row) {
            this.line = line;
            this.column = column;
            this.row = row;
        }

        public Position(int line, int column) {
            this.line = line;
            this.column = column;
        }

        static Position parse(String position) {
            Matcher m = PositionPattern.matcher(position);
            if (m.find()){
                return new Position(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
            }

            throw new IllegalStateException("Position is not valid");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Position)) return false;

            Position position = (Position) o;

            if (line != position.line) return false;
            return column == position.column;

        }

        @Override
        public int hashCode() {
            int result = line;
            result = 31 * result + column;
            return result;
        }

        @Override
        public String toString() {
            return line + "-" + column;
        }
    }
}
