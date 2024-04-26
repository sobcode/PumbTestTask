package com.projects.pumbtesttask.service.impl;

import com.projects.pumbtesttask.model.Animal;
import com.projects.pumbtesttask.model.dto.AnimalDTO;
import com.projects.pumbtesttask.model.dto.PaginatedAnimalResponseDTO;
import com.projects.pumbtesttask.repository.AnimalRepository;
import com.projects.pumbtesttask.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    @Override
    public PaginatedAnimalResponseDTO readAnimals(String type, String category, String sex, Pageable pageable) {
        Page<Animal> animals;

        if(Pattern.matches("[1-4]", category)) {
            animals = animalRepository.findAllByTypeContainsAndCategoryEqualsAndSexLike
                    (type, Integer.parseInt(category), sex + "%", pageable);
        } else {
            animals = animalRepository.findAllByTypeContainsAndSexLike(type, sex + "%", pageable);
        }

        return new PaginatedAnimalResponseDTO(animals.getContent()
                .stream()
                .map(a -> new AnimalDTO(a.getName(), a.getType(), a.getSex(), a.getWeight(), a.getCost(), a.getCategory()))
                .collect(Collectors.toList()), animals.getTotalElements(), animals.getTotalPages());

    }
}
