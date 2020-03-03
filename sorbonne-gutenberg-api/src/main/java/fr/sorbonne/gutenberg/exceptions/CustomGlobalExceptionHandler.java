package fr.sorbonne.gutenberg.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger EXCEPTION_LOGGER = LoggerFactory.getLogger(CustomGlobalExceptionHandler.class);

    @ExceptionHandler(FileStoreException.class)
    public ResponseEntity<Object> createFileStorageException(FileStoreException ex, WebRequest request) {
        Map<String, Object> body = new HttpErrorsBuilder().addError(ex.getMessage()).build();
        EXCEPTION_LOGGER.warn("File Storage failed : {} ", body);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
