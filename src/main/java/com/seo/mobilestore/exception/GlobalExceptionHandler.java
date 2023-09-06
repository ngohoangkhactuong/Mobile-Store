package com.seo.mobilestore.exception;

import com.seo.mobilestore.data.dto.ErrorMessageResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return createErrorResponse(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflictException(ConflictException ex, HttpServletRequest request) {
        return createErrorResponse(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex, HttpServletRequest request) {
        return createErrorResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return createErrorResponse(ex, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(JsonProcessException.class)
    public ResponseEntity<?> handleJsonProcessException(JsonProcessException ex, HttpServletRequest request) {
        return createErrorResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<?> handleInternalServerErrorException(
            InternalServerErrorException ex, HttpServletRequest request) {
        return createErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidExcelVersionException.class)
    public ResponseEntity<?> handleJInvalidExcelVersion(InvalidExcelVersionException ex, HttpServletRequest request) {
        return createErrorResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(java.lang.NullPointerException.class)
//    public ResponseEntity<?> handleNullPointer(java.lang.NullPointerException ex, HttpServletRequest request) {
//        return createErrorResponse(ex, request, HttpStatus.NO_CONTENT);
//    }

    @ExceptionHandler(CannotDeleteException.class)
    public ResponseEntity<?> handleCannotDeleteException(CannotDeleteException ex, HttpServletRequest request) {
        return createErrorResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> createErrorResponse(ExceptionCustom ex, HttpServletRequest request, HttpStatus status) {
        ErrorMessageResponseDTO errorResponse = new ErrorMessageResponseDTO(
                ex.getMessage(), ex.getErrors(), request.getServletPath());
        return ResponseEntity.status(status).body(errorResponse);
    }



}