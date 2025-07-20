package com.mcyldz.hrinventory.exception.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final int status;
    private final String error;
    private final String errorCode;
    private final String message;
    private final String path;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private List<ValidationErrorResponse> validationErrors;
}
