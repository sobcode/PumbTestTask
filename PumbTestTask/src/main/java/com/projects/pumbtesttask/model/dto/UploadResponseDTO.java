package com.projects.pumbtesttask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class represents a Data Transfer Object (DTO) used to structure the response for animal data upload.
 */
@Data
@AllArgsConstructor
public class UploadResponseDTO {
    private String message;
}
