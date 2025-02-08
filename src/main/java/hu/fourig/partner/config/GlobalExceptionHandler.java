package hu.fourig.partner.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseStatusException handleGenericException(ResponseStatusException ex) {
        log.warn("Handled exception: {}", ex.getReason());
        return ex;
    }

    @ExceptionHandler(Exception.class)
    public ResponseStatusException handleGenericException(Exception ex) {
        log.error("Unexpected exception: ", ex);
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}