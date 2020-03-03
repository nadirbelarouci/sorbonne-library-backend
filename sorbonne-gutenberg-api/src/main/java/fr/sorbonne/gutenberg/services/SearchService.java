package fr.sorbonne.gutenberg.services;

import fr.sorbonne.gutenberg.beans.BooksRanking;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SearchService {

    private BooksRanking booksRanking;
    private BookService bookService;

    public void searchByValue(String value) {

    }

    public void searchByRegex(String pattern) {

    }

    public void searchBookByValue(String book, String value) {

    }

    public void searchBookByRegex(String book, String pattern) {

    }

}
