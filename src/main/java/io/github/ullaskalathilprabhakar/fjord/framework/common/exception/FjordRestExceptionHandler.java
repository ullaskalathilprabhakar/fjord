package io.github.ullaskalathilprabhakar.fjord.framework.common.exception;


import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindException;
import java.util.List;


public abstract class FjordRestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(FjordRestExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.info("RestExceptionHandler -> handleValidationExceptions ", ex);
        BindingResult result = ex.getBindingResult();
        List<String> errors = result.getAllErrors().stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());
        ErrorResponseDTO errorvo = new ErrorResponseDTO();
        errorvo.setError("InvalidInput");
        errorvo.setMessage("InvalidInput");
        errorvo.setDetails(errors);
        return new ResponseEntity<ErrorResponseDTO>(errorvo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDTO> handleBindExceptions(BindException ex) {
        logger.info("RestExceptionHandler -> handleBindExceptions ", ex);
        BindingResult result = ex.getBindingResult();
        List<String> errors = result.getAllErrors().stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());
        ErrorResponseDTO errorvo = new ErrorResponseDTO();
        errorvo.setError("Bind exception");
        errorvo.setMessage("Bind exception");
        errorvo.setDetails(errors);
        return new ResponseEntity<ErrorResponseDTO>(errorvo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleUnknownException(Exception ex ) {

        logger.error("RestExceptionHandler -> handleUnknownException ", ex);
        ErrorResponseDTO errorvo = new ErrorResponseDTO();
        errorvo.setError("Unknown Error!!!");
        errorvo.setMessage("Unknown Error!!!");
        return new ResponseEntity<ErrorResponseDTO>(errorvo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

