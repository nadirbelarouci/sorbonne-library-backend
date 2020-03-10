package fr.sorbonne.gutenberg.api;

import fr.sorbonne.gutenberg.services.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/search")
public class SearchApi {

    private SearchService searchService;

    @GetMapping("/{value}")
    public ResponseEntity<String> searchByValue(@PathVariable String value,
                                                @RequestParam(required = false) Integer start) {

        return ResponseEntity.ok("");
    }

    @GetMapping("/regex/{pattern}")
    public ResponseEntity<String> searchByRegex(@PathVariable String pattern,
                                                @RequestParam(required = false) Integer start) {

        return ResponseEntity.ok("");
    }



}
