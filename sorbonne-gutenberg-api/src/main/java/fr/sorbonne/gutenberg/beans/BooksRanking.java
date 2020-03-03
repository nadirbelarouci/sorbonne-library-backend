package fr.sorbonne.gutenberg.beans;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class BooksRanking {
    private List<String> books;

    public BooksRanking(InputStream stream) {
        books = new BufferedReader(new InputStreamReader(stream))
                .lines()
                .collect(Collectors.toList());
    }

    public Stream<String> stream() {
        return books.stream();
    }
}
