package com.projects.pumbtesttask.service.impl;

import com.projects.pumbtesttask.helper.XMLHelper;
import com.projects.pumbtesttask.model.Animal;
import com.projects.pumbtesttask.repository.AnimalRepository;
import com.projects.pumbtesttask.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class XMLServiceImpl implements FileService {
    private final AnimalRepository animalRepository;

    @Autowired
    public XMLServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Override
    public void save(MultipartFile file) {
        try {
            List<Animal> animals = XMLHelper.xmlToAnimals(file.getInputStream());
            animalRepository.saveAll(animals);

        } catch (IOException e) {
            throw new RuntimeException("Fail to store data from XML file: " + e);
        }
    }
}
