package com.parmida98.skincare_backend.error_handling;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice // är en global REST-felhanterare för alla controllers
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 400 - validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidation(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<ApiErrorDTO.FieldViolationDTO> violations = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toViolation)
                .toList();

        logger.warn("Validation failed on {}: {}", request.getRequestURI(), violations);

        return respond(HttpStatus.BAD_REQUEST, "Validation failed", request, violations);
    }

    // 400 - missing query param
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorDTO> handleMissingParam(MissingServletRequestParameterException e, HttpServletRequest request) {
        logger.warn("Missing request parameter on {}: {}", request.getRequestURI(), e.getMessage());

        return respond(HttpStatus.BAD_REQUEST, "Missing required parameter: " + e.getParameterName(), request, List.of());
    }

    // 400 -unreadable, wrong types...
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorDTO> handleNotReadable(HttpMessageNotReadableException e, HttpServletRequest request) {
        logger.warn("Unreadable request body on {}", request.getRequestURI());
        logger.debug("Unreadable request body details on {}: {}", request.getRequestURI(), e.getMessage(), e);

        return respond(HttpStatus.BAD_REQUEST, "Request body is unreadable", request, List.of());
    }

    // 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorDTO> handleIllegalArgument(IllegalArgumentException e, HttpServletRequest request) {
        logger.warn("Bad request on {}: {}", request.getRequestURI(), e.getMessage());

        return respond(HttpStatus.BAD_REQUEST, e.getMessage(), request, List.of());
    }

    // 404 - no handler found
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleNotFound(NoHandlerFoundException e, HttpServletRequest request) {
        logger.warn("Not found: {} {}", e.getHttpMethod(), e.getRequestURL());

        return respond(HttpStatus.NOT_FOUND, "Endpoint not found", request, List.of());
    }

    // other errors
    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ApiErrorDTO> handleErrorResponse(ErrorResponseException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
        logger.warn("ErrorResponse on {}: {} ({})", request.getRequestURI(), e.getMessage(), status);

        return respond(status, e.getMessage(), request, List.of());
    }

    // 500 - fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGeneric(Exception e, HttpServletRequest request) {
        logger.error("Internal server error on: {}: {}", request.getRequestURI(), e.getMessage(), e);

        return respond(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request, List.of());
    }


    private ResponseEntity<ApiErrorDTO> respond(
            HttpStatus status,
            String message,
            HttpServletRequest request,
            List<ApiErrorDTO.FieldViolationDTO> violations
    ) {
        ApiErrorDTO body = build(status, message, request, violations);
        return ResponseEntity.status(status).body(body);
    }

    private ApiErrorDTO build(
            HttpStatus status,
            String message,
            HttpServletRequest request,
            List<ApiErrorDTO.FieldViolationDTO> violations
    ) {
        List<ApiErrorDTO.FieldViolationDTO> safeViolations = (violations == null) ? List.of() : violations;

        return new ApiErrorDTO(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                safeViolations
        );
    }

    private ApiErrorDTO.FieldViolationDTO toViolation(FieldError fieldError) {
        String message = fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Invalid value";
        return new ApiErrorDTO.FieldViolationDTO(fieldError.getField(), message);
    }
}
