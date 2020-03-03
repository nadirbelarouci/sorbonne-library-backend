package fr.sorbonne.gutenberg.config;

import fr.sorbonne.gutenberg.beans.BooksRanking;
import fr.sorbonne.gutenberg.services.S3FileStore;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
@AllArgsConstructor
public class BooksRankingConfiguration {

    private FileStoreProperties fileStore;
    private S3FileStore s3FileStore;

    @Bean
    public BooksRanking booksRanking() {
        InputStream is = s3FileStore.download(fileStore.getBooksIndicesPath(), fileStore.getBooksRankingFile());
        return new BooksRanking(is);
    }
}
