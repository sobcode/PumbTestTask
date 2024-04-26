package com.projects.pumbtesttask.service;

import com.projects.pumbtesttask.model.Animal;
import com.projects.pumbtesttask.model.dto.AnimalDTO;
import com.projects.pumbtesttask.model.dto.PaginatedAnimalResponseDTO;
import org.springframework.data.domain.Pageable;

public interface AnimalService {
    Animal createAnimal(AnimalDTO animalDTO);
    PaginatedAnimalResponseDTO readAnimals(String type, String category, String sex, Pageable pageable);
}
