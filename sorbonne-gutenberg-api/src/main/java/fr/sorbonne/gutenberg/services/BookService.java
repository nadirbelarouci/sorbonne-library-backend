package fr.sorbonne.gutenberg.services;


import com.amazonaws.util.IOUtils;
import fr.sorbonne.gutenberg.config.FileStoreProperties;
import fr.sorbonne.gutenberg.core.RadixTree;
import fr.sorbonne.gutenberg.exceptions.FileStoreException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);


    private final FileStoreService fileStoreService;
    private final FileStoreProperties fileStoreProperties;


    /**
     * Upload a book to the storage store
     *
     * @param book the id of the candidate
     *             =     * @return the filename of the book
     */
    public String uploadBook(MultipartFile book) {

        checkIsEmptyThenThrow(book);
        checkIsTxtThenThrow(book);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", book.getContentType());
        metadata.put("Content-Length", String.valueOf(book.getSize()));

        try {
            String path = String.format("%s", fileStoreProperties.getBooksPath());
            String filename = String.format("%s-%s", book.getOriginalFilename(), UUID.randomUUID());
            fileStoreService.save(path, filename, Optional.of(metadata), book.getInputStream());
            return filename;
        } catch (IOException | FileStoreException e) {
            logger.error("Uploading book failed : {}", e.getMessage());
            throw new FileStoreException("file.upload.failed");
        }
    }

    /**
     * Get a book
     *
     * @param bookName the book id
     * @return the book file as byte stream
     */
    public byte[] downloadBook(String bookName) {
        download(fileStoreProperties.getBooksIndicesPath(), bookName);
        try {
            return IOUtils.toByteArray(download(fileStoreProperties.getBooksIndicesPath(), bookName));
        } catch (IOException e) {
            logger.error("Downloading book failed : {}", e.getMessage());
            throw new FileStoreException("file.download.failed");
        }
    }

    /**
     * Get an Index
     *
     * @param indexName the Index id
     * @return the book file as byte stream
     */
    @Cacheable
    public RadixTree downloadIndex(String indexName) {
        return new RadixTree(download(fileStoreProperties.getBooksIndicesPath(), indexName));
    }

    private InputStream download(String path, String name) {
        try {
            return fileStoreService.download(path, name);
        } catch (FileStoreException e) {
            logger.error("Downloading book failed : {}", e.getMessage());
            throw new FileStoreException("file.download.failed");
        }
    }

    /**
     * Check if the book file size is lower then maximum allowed size
     */
    private void checkIsMaxSizeThenThrow(MultipartFile book) {
        if (book.getSize() > (fileStoreProperties.getBookMaxFileSize() * 1000000)) {
            logger.error("Uploading book failed : file is too big");
            throw new FileStoreException("file.upload.max-size");
        }
    }

    /**
     * Check if the file is of type PDF
     */
    private void checkIsTxtThenThrow(MultipartFile book) {
        if (book.getContentType() != null && !book.getContentType().toLowerCase().contains("text/plain")) {
            logger.error("Uploading book failed : not a PDF file");
            throw new FileStoreException("file.upload.invalid-type");
        }
    }

    /**
     * Check if the file uploaded is empty
     */
    private void checkIsEmptyThenThrow(MultipartFile book) {
        if (book.isEmpty()) {
            logger.error("Uploading book failed : empty file");
            throw new FileStoreException("file.upload.empty");
        }
    }


}
