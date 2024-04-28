package com.projects.pumbtesttask.service.impl;

import com.projects.pumbtesttask.helper.CSVHelper;
import com.projects.pumbtesttask.helper.XMLHelper;
import com.projects.pumbtesttask.model.Animal;
import com.projects.pumbtesttask.model.dto.AnimalDTO;
import com.projects.pumbtesttask.model.dto.PaginatedAnimalResponseDTO;
import com.projects.pumbtesttask.model.dto.UploadResponseDTO;
import com.projects.pumbtesttask.repository.AnimalRepository;
import com.projects.pumbtesttask.service.AnimalService;
import com.projects.pumbtesttask.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class implements the AnimalService interface and provides business logic for animal data management.
 */
@Service
public class AnimalServiceImpl implements AnimalService {
    private static final Logger log = LoggerFactory.getLogger(AnimalServiceImpl.class);

    private final AnimalRepository animalRepository;
    private final FileService csvService;
    private final FileService xmlService;
    private final XMLHelper xmlHelper;
    private final CSVHelper csvHelper;

    @Autowired
    public AnimalServiceImpl(AnimalRepository animalRepository,
                             @Qualifier("CSVServiceImpl") FileService csvService,
                             @Qualifier("XMLServiceImpl") FileService xmlService,
                             XMLHelper xmlHelper, CSVHelper csvHelper) {
        this.animalRepository = animalRepository;
        this.csvService = csvService;
        this.xmlService = xmlService;
        this.xmlHelper = xmlHelper;
        this.csvHelper = csvHelper;
    }

    /**
     * This method searches for animals in the database using the AnimalRepository.
     * It allows filtering by animal type, category, and sex.
     * The search results are paginated using the provided Pageable object.
     *
     * @param type (Optional) The type of animal to search for.
     * @param category (Optional) The category of the animal to search for.
     * @param sex (Optional) The sex of the animal to search for.
     * @param pageable The Pageable object specifying sorting and pagination options for the results.
     * @return A PaginatedAnimalResponseDTO object containing the search results and pagination information.
     * @throws IllegalArgumentException If the sort fields in the pageable argument are invalid.
     */
    @Override
    public PaginatedAnimalResponseDTO readAnimals(String type, String category, String sex, Pageable pageable) {
        if(!sortIsValid(pageable.getSort())) {
            String message = "Invalid sort field!";
            log.warn("LOG: " + message);
            throw new IllegalArgumentException(message);
        }

        Page<Animal> animals;

        if(Pattern.matches("[1-4]", category)) {
            // Search with category filtering
            animals = animalRepository.findAllByTypeContainsAndCategoryEqualsAndSexLike
                    (type, Integer.parseInt(category), sex + "%", pageable);
        } else {
            // Search without category filtering
            animals = animalRepository.findAllByTypeContainsAndSexLike(type, sex + "%", pageable);
        }

        return new PaginatedAnimalResponseDTO(animals.getContent()
                .stream()
                .map(a -> new AnimalDTO(a.getName(), a.getType(), a.getSex(), a.getWeight(), a.getCost(), a.getCategory()))
                .collect(Collectors.toList()), animals.getTotalElements(), animals.getTotalPages());

    }

    /**
     * This method first checks the file format using the XMLHelper and CSVHelper classes.
     * Based on the file format (CSV or XML), it delegates the saving logic
     * to the corresponding FileService implementation (csvService or xmlService).
     *
     * @param file The MultipartFile object representing the uploaded file.
     * @return A ResponseEntity object with an UploadResponseDTO containing a success message.
     * @throws IllegalArgumentException If the uploaded file format is not CSV or XML.
     * @throws RuntimeException If there's an error during file saving.
     */
    @Override
    public ResponseEntity<UploadResponseDTO> uploadAnimalsFromCsvOrXmlFile(MultipartFile file) {
        String message;
        try {
            if (csvHelper.hasCSVFormat(file)) {
                csvService.save(file);
                message = "The csv file uploaded successfully";
                return ResponseEntity.ok(new UploadResponseDTO(message));
            }
            else if (xmlHelper.hasXMLFormat(file)) {
                xmlService.save(file);
                message = "The xml file uploaded successfully";
                return ResponseEntity.ok(new UploadResponseDTO(message));
            }
        } catch (Exception e) {
            message = "Could not upload the file " + file.getOriginalFilename() + "! " + e;
            log.error("LOG: an error occurred " + e);
            throw new RuntimeException(message);
        }
        message = "Illegal file format. Please upload a csv or xml file!";
        log.warn("LOG: Illegal file format.");
        throw new IllegalArgumentException(message);
    }

    /**
     * This method checks if the sort fields within the Pageable argument are valid for animal data searching.
     *
     * @param sort The Sort object from the Pageable argument containing sort information.
     * @return True if all sort fields are valid, False otherwise.
     */
    private boolean sortIsValid(Sort sort) {
        List<String> validSortFields = Arrays.asList("name", "type", "sex", "weight", "cost", "category");

        if(sort != null) {
            // iterates through the sort orders and ensures the sort field names
            // are present among the allowed fields
            for(Sort.Order order : sort) {
                String fieldName = order.getProperty();

                if(!validSortFields.contains(fieldName)) {
                    log.warn("LOG: sort is invalid.");
                    return false;
                }
            }
        }
        return true;
    }
}
