package com.projects.pumbtesttask.service;

import com.projects.pumbtesttask.model.Animal;
import com.projects.pumbtesttask.model.dto.AnimalDTO;

public interface AnimalService {
    Animal createAnimal(AnimalDTO animalDTO);
}
