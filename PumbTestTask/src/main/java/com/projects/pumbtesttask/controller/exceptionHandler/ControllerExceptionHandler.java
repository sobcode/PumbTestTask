package com.projects.pumbtesttask.controller.exceptionHandler;

import com.projects.pumbtesttask.aspect.LoggingAspect;
import com.projects.pumbtesttask.model.dto.ExceptionResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class is a global exception handler for controllers.
 * This class can handle exceptions thrown by any controller within the application.
 */
@RestControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * This method is a global exception handler.
     * When an exception occurs, this method is invoked and creates an `ExceptionResponseDTO` object with details
     * about the exception, including the HTTP status code, message, and exception class name.

     * @param exception The exception thrown by the controller method.
     * @return A ResponseEntity object containing an ExceptionResponseDTO with details about the exception.
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionResponseDTO> handleException(Exception exception) {
        log.warn("LOG: handling exception " + exception);

        return provideResponseEntity(HttpStatus.BAD_REQUEST,
                exception.getMessage(), exception.getClass().getSimpleName());
    }

    /**
     * Helper method to create a ResponseEntity object with an ExceptionResponseDTO.
     *
     * This method takes the HTTP status code, exception message, and exception class name as arguments
     * and constructs an ExceptionResponseDTO object. It then creates a ResponseEntity object with the
     * constructed ExceptionResponseDTO and the provided HTTP status code.
     *
     * @param status The HTTP status code for the response.
     * @param message The exception message.
     * @param simpleName The simple name of the exception class.
     * @return A ResponseEntity object containing an ExceptionResponseDTO with exception details.
     */
    private ResponseEntity<ExceptionResponseDTO> provideResponseEntity(HttpStatus status, String message,
                                                                       String simpleName) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(status.value(), message, simpleName);

        return new ResponseEntity<>(responseDTO, status);
    }
}
