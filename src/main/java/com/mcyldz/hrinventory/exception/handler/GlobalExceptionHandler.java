package com.mcyldz.hrinventory.exception.handler;

import com.mcyldz.hrinventory.exception.dto.response.ErrorResponse;
import com.mcyldz.hrinventory.exception.dto.response.ValidationErrorResponse;
import com.mcyldz.hrinventory.exception.model.ApplicationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex, WebRequest request){

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getStatus().value(),
                ex.getStatus().getReasonPhrase(),
                ex.getErrorCode().name(),
                ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllOtherExceptions(Exception ex, WebRequest request){

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "INTERNAL_SERVER_ERROR",
                "Beklenmedik hata oluştu. Lütfen daha sonra tekrar deneyin.",
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                "Bad Request",
                "VALIDATION_ERROR",
                "Validation hatası. Lütfen daha sonra tekrar deneyin.",
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );

        List<ValidationErrorResponse> validationErrors = new ArrayList<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        for (FieldError fieldError : fieldErrors){
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            ValidationErrorResponse validationError = new ValidationErrorResponse(fieldName, errorMessage);
            validationErrors.add(validationError);
        }

        errorResponse.setValidationErrors(validationErrors);

        return new ResponseEntity<>(errorResponse, headers, status);
    }
}