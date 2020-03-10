package fr.sorbonne.gutenberg.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import fr.sorbonne.gutenberg.model.Book;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DynamoDBService {
    private DynamoDBMapper mapper;

    public Book getBookById(String bookId) {
        return mapper.load(Book.class, bookId);
    }
}
