package fr.sorbonne.gutenberg.services;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public interface FileStoreService {

    void save(String path, String filename, Optional<Map<String, String>> optionalMetadata, InputStream inputStream);

    InputStream download(String path, String filename);
}
