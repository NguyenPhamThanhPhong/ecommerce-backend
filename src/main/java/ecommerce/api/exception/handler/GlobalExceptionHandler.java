package ecommerce.api.exception.handler;

import ecommerce.api.dto.exception.ErrorResponse;
import ecommerce.api.dto.exception.FieldErrorResponse;
import ecommerce.api.exception.BadRequestException;
import ecommerce.api.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String COMMON_ERROR_MESSAGE_TEMPLATE = "Got error: [%s], with Message: [%s]";

    private String buildErrorMessage(Exception ex) {
        return String.format(COMMON_ERROR_MESSAGE_TEMPLATE, ex.getClass().getName(), ex.getMessage());
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse<?> handleUnCaughtException(Exception ex, WebRequest webRequest) {
//        log.error(buildErrorMessage(ex));
//        return new ErrorResponse<>(webRequest.getContextPath(), ex.getMessage(), null);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse<List<FieldErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error(buildErrorMessage(ex));
        List<FieldErrorResponse> errorDTOs = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorResponse(error.getField(), String.valueOf(error.getRejectedValue()))).toList();
        return new ErrorResponse<List<FieldErrorResponse>>(request.getContextPath(), ex.getMessage(), errorDTOs);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse<?> handleSchedulerException(BadRequestException ex, HttpServletRequest webRequest) {
        return new ErrorResponse<>(webRequest.getContextPath(), ex.getMessage(), null);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse<?> handleResourceAccessException(ResourceNotFoundException ex, HttpServletRequest webRequest) {
        return new ErrorResponse<>(webRequest.getContextPath(), ex.getMessage(), null);
    }
}
