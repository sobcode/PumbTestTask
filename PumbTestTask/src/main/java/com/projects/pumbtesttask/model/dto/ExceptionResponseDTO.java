package com.projects.pumbtesttask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a Data Transfer Object (DTO) used to structure the response for exceptions.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponseDTO {
    private int status; // HTTP status code
    private String message;
    private String exceptionName;
}
