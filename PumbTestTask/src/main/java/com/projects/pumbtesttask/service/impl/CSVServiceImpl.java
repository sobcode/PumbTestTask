package com.projects.pumbtesttask.service.impl;

import com.projects.pumbtesttask.helper.CSVHelper;
import com.projects.pumbtesttask.model.Animal;
import com.projects.pumbtesttask.repository.AnimalRepository;
import com.projects.pumbtesttask.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CSVServiceImpl implements CSVService {
    private final AnimalRepository animalRepository;

    @Autowired
    public CSVServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Override
    public void save(MultipartFile file) {
        try {
            List<Animal> animals = CSVHelper.csvToAnimals(file.getInputStream());
            animalRepository.saveAll(animals);

        } catch (IOException e) {
            throw new RuntimeException("Fail to store data from CSV file: " + e);
        }
    }
}
