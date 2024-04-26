package com.projects.pumbtesttask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedAnimalResponseDTO {
    private List<AnimalDTO> animalList;
    private long numberOfItems;
    private int numberOfPages;
}
