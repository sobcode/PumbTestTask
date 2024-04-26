package com.projects.pumbtesttask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponseDTO {
    private int status;
    private String message;
    private String exceptionName;
}
