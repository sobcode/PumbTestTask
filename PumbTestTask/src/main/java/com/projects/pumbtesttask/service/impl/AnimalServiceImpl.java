package com.projects.pumbtesttask.service.impl;

import com.projects.pumbtesttask.model.Animal;
import com.projects.pumbtesttask.model.dto.AnimalDTO;
import com.projects.pumbtesttask.repository.AnimalRepository;
import com.projects.pumbtesttask.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;

    @Autowired
    public AnimalServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Override
    public Animal createAnimal(AnimalDTO animalDTO) {
        return animalRepository.save(Animal.getAnimalFromAnimalDTO(animalDTO));
    }
}
