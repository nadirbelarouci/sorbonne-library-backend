package fr.sorbonne.gutenberg;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class SaveBookHandler {
    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();

    public static void save(Book book) {
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.save(book);
    }
}