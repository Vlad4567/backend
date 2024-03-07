package com.example.beautybook.exceptions;

import com.example.beautybook.exceptions.photo.EmptyPhotoException;
import com.example.beautybook.exceptions.photo.GalleryLimitExceededException;
import com.example.beautybook.exceptions.photo.InvalidOriginFileNameException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
        Map<String, String> errors = new HashMap<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String[] errorMessage = getErrorMessage(error);
            errors.put(errorMessage[0], errorMessage[1]);
        }
        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex) {
        return getResponse(ex, 400);
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
    public ResponseEntity<Object> handleGalleryLimitExceededException(
            GalleryLimitExceededException ex) {
        return getResponse(ex, 400);
    }

    @ExceptionHandler(InvalidOriginFileNameException.class)
    public ResponseEntity<Object> handleInvalidOriginFileNameException(
            InvalidOriginFileNameException ex) {
        return getResponse(ex, 400);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleException(ExpiredJwtException ex) {
        return getResponse(ex, 401);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleInvocationTargetException(
            SQLIntegrityConstraintViolationException ex) {
        return getResponse(ex, 401);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<Object> handleLoginException(LoginException ex) {
        return getResponse(ex, 401);
    }

    private String[] getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            String field = ((FieldError) e).getField();
            String message = e.getDefaultMessage();
            return new String[]{field, message};
        }
        return new String[]{e.getDefaultMessage()};
    }

    private ResponseEntity<Object> getResponse(Exception ex, int statusCode) {
        Object error = ex.getMessage();
        if (ex instanceof RegistrationException) {
            Map<String, String> errorMassage = new LinkedHashMap<>();
            String[] lines = ex.getMessage().split(System.lineSeparator());
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    errorMassage.put(parts[0].trim(), parts[1].trim());
                }
            }
            error = errorMassage;
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatusCode.valueOf(statusCode));
        body.put("error", error);
        return new ResponseEntity<>(body, HttpStatus.valueOf(statusCode));
    }
}
