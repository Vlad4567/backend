package com.example.beautybook.exceptions;

import com.example.beautybook.exceptions.photo.EmptyPhotoException;
import com.example.beautybook.exceptions.photo.GalleryLimitExceededException;
import com.example.beautybook.exceptions.photo.InvalidOriginFileNameException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST);
        List<String> errors =
                ex.getBindingResult().getAllErrors().stream()
                        .map(this::getErrorMessage)
                        .toList();
        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        return getResponse(ex, 404);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Object> handleRegistrationException(RegistrationException ex) {
        return getResponse(ex, 409);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        return getResponse(ex, 404);
    }

    @ExceptionHandler(UnverifiedUserException.class)
    public ResponseEntity<Object> handleUnverifiedUserException(UnverifiedUserException ex) {
        return getResponse(ex, 400);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<Object> handleFileUploadException(FileUploadException ex) {
        return getResponse(ex, 400);
    }

    @ExceptionHandler(VirusDetectionException.class)
    public ResponseEntity<Object> handleVirusDetectionException(VirusDetectionException ex) {
        return getResponse(ex, 400);
    }

    @ExceptionHandler(EmptyPhotoException.class)
    public ResponseEntity<Object> handleEmptyPhotoException(EmptyPhotoException ex) {
        return getResponse(ex, 400);
    }

    @ExceptionHandler(GalleryLimitExceededException.class)
    public ResponseEntity<Object> handleGalleryLimitExceededException
            (
                    GalleryLimitExceededException ex
            ) {
        return getResponse(ex, 400);
    }

    @ExceptionHandler(InvalidOriginFileNameException.class)
    public ResponseEntity<Object> handleInvalidOriginFileNameException
            (
                    InvalidOriginFileNameException ex
            ) {
        return getResponse(ex, 400);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleException(ExpiredJwtException ex) {
        return getResponse(ex, 401);
    }

    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            String field = ((FieldError) e).getField();
            String message = e.getDefaultMessage();
            return field + " " + message;
        }
        return e.getDefaultMessage();
    }

    private ResponseEntity<Object> getResponse(Exception ex, int statusCode) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatusCode.valueOf(statusCode));
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.valueOf(statusCode));
    }
}
