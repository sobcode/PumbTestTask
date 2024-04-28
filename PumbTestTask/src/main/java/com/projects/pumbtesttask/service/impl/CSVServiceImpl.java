package com.projects.pumbtesttask.service.impl;

import com.projects.pumbtesttask.helper.CSVHelper;
import com.projects.pumbtesttask.model.Animal;
import com.projects.pumbtesttask.repository.AnimalRepository;
import com.projects.pumbtesttask.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * This class implements the FileService interface and provides functionalities for saving animal data from CSV files.
 */
@Service
public class CSVServiceImpl implements FileService {
    private static final Logger log = LoggerFactory.getLogger(CSVServiceImpl.class);
    private final AnimalRepository animalRepository;

    @Autowired
    public CSVServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    /**
     * This method reads animal data from the provided MultipartFile object.
     * It uses a CSVHelper class to parse the CSV content and convert it into a list of Animal objects.
     * The list of Animal objects is then saved to the database using the AnimalRepository.
     *
     * @param file The MultipartFile object representing the uploaded CSV file.
     * @throws RuntimeException If there's an error during CSV parsing or saving data to the repository.
     */
    @Override
    public void save(MultipartFile file) {
        CSVHelper csvHelper = new CSVHelper();
        try {
            List<Animal> animals = csvHelper.csvToAnimals(file.getInputStream());
            animalRepository.saveAll(animals);

        } catch (IOException e) {
            log.error("LOG: an error occurred " + e);
            throw new RuntimeException("Fail to store data from CSV file: " + e);
        }
    }
}
