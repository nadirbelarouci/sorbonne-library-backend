package fr.sorbonne.gutenberg.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import fr.sorbonne.gutenberg.exceptions.FileStoreException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class S3FileStore implements FileStoreService {

    private static final Logger logger = LoggerFactory.getLogger(S3FileStore.class);

    private final AmazonS3 s3;

    /**
     * Upload a file to S3 bucket
     *
     * @param path        the path in the bucket to where to upload the file
     * @param filename    the key of the file on the bucket
     * @param metadata    the file metadata options
     * @param inputStream the file as inputStream
     */
    public void save(String path,
                     String filename,
                     Optional<Map<String, String>> metadata,
                     InputStream inputStream) {
        logger.info("Requesting to upload a file {} to {} ", filename, path);
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            metadata.ifPresent(map -> {
                if (!map.isEmpty()) {
                    map.forEach(objectMetadata::addUserMetadata);
                }
            });
            s3.putObject(path, filename, inputStream, objectMetadata);
        } catch (Exception ex) {
            logger.error("Failed to save file to S3 bucket storage {}", ex.getMessage());
            throw new FileStoreException("file.upload.failed");
        }
    }

    /**
     * Retrieve file from the S3 bucket
     *
     * @param path     the path to the file
     * @param filename the key of the file on the bucket
     * @return the file as byte of array
     */
    public InputStream download(String path, String filename) {
        logger.info("Requesting to download the file {} from {} ", filename, path);
        try {
            S3Object object = s3.getObject(path, filename);
            return object.getObjectContent();
        } catch (AmazonServiceException ex) {
            logger.error("Failed to retrieve file from S3 bucket : {}", ex.getMessage());
            throw new FileStoreException("file.download.failed");
        }

    }
}
