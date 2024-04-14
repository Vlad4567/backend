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

    @ExceptionHandler({
            ConstraintViolationException.class,
            UnverifiedUserException.class,
            FileUploadException.class,
            VirusDetectionException.class,
            EmptyPhotoException.class,
            GalleryLimitExceededException.class,
            InvalidOriginFileNameException.class,
            SQLIntegrityConstraintViolationException.class,
            TelegramException.class
    })
    public ResponseEntity<Object> handleBadRequestExceptions(
            Exception ex) {
        return getResponse(ex, 400);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleAuthenticationExceptions(Exception ex) {
        return getResponse(ex, 401);
    }

    @ExceptionHandler({
            LoginException.class,
            LoginDeviceLimitExceededException.class,
    })
    public ResponseEntity<Object> handleLoginExceptions(Exception ex) {
        return getResponse(ex, 403);
    }

    @ExceptionHandler({
            EntityNotFoundException.class,
            AccessDeniedException.class
    })
    public ResponseEntity<Object> handleResourceAccessExceptions(Exception ex) {
        return getResponse(ex, 404);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Object> handleRegistrationException(RegistrationException ex) {
        return getResponse(ex, 409);
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
