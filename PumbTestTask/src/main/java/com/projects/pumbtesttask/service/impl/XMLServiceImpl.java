package com.projects.pumbtesttask.service.impl;

import com.projects.pumbtesttask.helper.XMLHelper;
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
 * This class implements the FileService interface and provides functionalities for saving animal data from XML files.
 */
@Service
public class XMLServiceImpl implements FileService {
    private final AnimalRepository animalRepository;
    private static final Logger log = LoggerFactory.getLogger(XMLServiceImpl.class);

    @Autowired
    public XMLServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    /**
     * This method reads animal data from the provided MultipartFile object.
     * It uses an XMLHelper class to parse the XML content and convert it into a list of Animal objects.
     * The list of Animal objects is then saved to the database using the AnimalRepository.
     *
     * @param file The MultipartFile object representing the uploaded XML file.
     * @throws RuntimeException If there's an error during XML parsing or saving data to the repository.
     */
    @Override
    public void save(MultipartFile file) {
        XMLHelper xmlHelper = new XMLHelper();
        try {
            List<Animal> animals = xmlHelper.xmlToAnimals(file.getInputStream());
            animalRepository.saveAll(animals);

        } catch (IOException e) {
            log.error("LOG: an error occurred " + e);
            throw new RuntimeException("Fail to store data from XML file: " + e);
        }
    }
}
