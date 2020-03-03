package fr.sorbonne.gutenberg.api;

import com.google.gson.Gson;
import fr.sorbonne.gutenberg.services.BookService;
import fr.sorbonne.gutenberg.services.SearchService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/book")
@AllArgsConstructor
public class BookApi {
    private static final Logger logger = LoggerFactory.getLogger(BookApi.class);
    private static Gson gson = new Gson();
    private BookService bookService;
    private SearchService searchService;

    @GetMapping("/{name}/{value}")
    public ResponseEntity<String> search(@PathVariable String name, @PathVariable String value) {
        return ResponseEntity.ok("");
    }

    @GetMapping("/{name}/regex/{value}")
    public ResponseEntity<String> searchRegex(@PathVariable String name, @PathVariable String value) {
        return ResponseEntity.ok("");
    }


    /**
     * POST  /api/book/upload : upload a book.
     *
     * @param file MultipartFile
     * @return the ResponseEntity with status 200 (OK), with body of the book file name
     */
    @PostMapping(value = "upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadBook(
            @RequestParam("book") MultipartFile file) {
        logger.debug("REST request to upload a book");
        String result = bookService.uploadBook(file);
        return ResponseEntity.ok(gson.toJson(result));
    }

    /**
     * GET  /api/book/:name/download : get a book.
     *
     * @param bookName the id of book
     * @return the ResponseEntity with status 200 (OK), with body of the book file bytes
     */
    @GetMapping(value = "/{name}/download", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadBook(@PathVariable("name") String bookName) {
        logger.debug("REST request to get a book : {}", bookName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline;");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bookService.downloadBook(bookName));
    }


}
