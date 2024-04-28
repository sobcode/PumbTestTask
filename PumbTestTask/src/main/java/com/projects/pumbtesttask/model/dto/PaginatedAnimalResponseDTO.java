package com.projects.pumbtesttask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * This class represents a Data Transfer Object (DTO) used
 * to structure the response for paginated animal search results.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedAnimalResponseDTO {
    private List<AnimalDTO> animalList;
    private long numberOfItems;
    private int numberOfPages;
}
