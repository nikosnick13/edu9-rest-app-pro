package gr.aueb.cf.eduapp.core;

import gr.aueb.cf.eduapp.core.exceptions.*;
import gr.aueb.cf.eduapp.dto.ErrorResponseDTO;
import gr.aueb.cf.eduapp.dto.ValidationResponseEntityDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j

public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationResponseEntityDTO> handleValidationError(ValidationException ex){

        log.warn("Validation Failed. Message= {}",ex.getMessage());

        BindingResult bindingResult =  ex.getBindingResult();

        Map<String,String> errors = new HashMap<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()){
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(new ValidationResponseEntityDTO(ex.getCode(), ex.getMessage(),errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(EntityNotFoundException ex){
        log.warn("Entity not found. Message= {}",ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) //404 not found
                .body(new ErrorResponseDTO(ex.getCode(),ex.getMessage()));

    }

    @ExceptionHandler(EntityInvalidArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(EntityInvalidArgumentException ex){
        log.warn("Invalid argument. Message= {}",ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) //400 bad request
                .body(new ErrorResponseDTO(ex.getCode(),ex.getMessage()));

    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(EntityAlreadyExistsException ex){
        log.warn("Entity already exist. Message= {}",ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)  //409 Conlict
                .body(new ErrorResponseDTO(ex.getCode(),ex.getMessage()));

    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(FileUploadException ex){
        log.warn("File upload fail. Message= {}",ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                .body(new ErrorResponseDTO(ex.getCode(),ex.getMessage()));
    }


    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(DataAccessException ex){
        log.warn("File upload fail. Message= {}",ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                .body(new ErrorResponseDTO("DATABASE_ERROR","A database error"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(Exception ex){
        log.warn("Unexpected error. Message= {}",ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                .body(new ErrorResponseDTO("INTERNAL_SERVER_ERROR","A Unexpected error"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationException(AuthenticationException e,
                                                                          HttpServletRequest request) {
        log.warn("Failed login for IP={}", request.getRemoteAddr());

        String errorCode = switch (e) {
            case BadCredentialsException ex -> "INVALID_CREDENTIALS";
            case DisabledException ex -> "ACCOUNT_DISABLED";
            case LockedException ex -> "ACCOUNT_LOCKED";
            case AccountExpiredException ex -> "ACCOUNT_EXPIRED";
            case CredentialsExpiredException ex -> "CREDENTIALS_EXPIRED";
            default -> "AUTHENTICATION_ERROR";
        };

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)            // 401 Unauthorized
                .body(new ErrorResponseDTO(errorCode, e.getMessage()));
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("Access denied. Message={}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)       // 403 Forbidden
                .body(new ErrorResponseDTO("ACCESS_DENIED", e.getMessage()));
    }
}

