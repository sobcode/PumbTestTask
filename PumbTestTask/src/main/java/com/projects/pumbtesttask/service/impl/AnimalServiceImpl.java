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

@Service
public class AnimalServiceImpl implements AnimalService {

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

    @Override
    public PaginatedAnimalResponseDTO readAnimals(String type, String category, String sex, Pageable pageable) {
        if(!sortIsValid(pageable.getSort())) {
            throw new IllegalArgumentException("Invalid sort field!");
        }

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
            throw new RuntimeException(message);
        }
        message = "Illegal file format. Please upload a csv or xml file!";
        throw new IllegalArgumentException(message);
    }

    private boolean sortIsValid(Sort sort) {
        List<String> validSortFields = Arrays.asList("name", "type", "sex", "weight", "cost", "category");

        if(sort != null) {
            for(Sort.Order order : sort) {
                String fieldName = order.getProperty();

                if(!validSortFields.contains(fieldName)) {
                    return false;
                }
            }
        }
        return true;
    }
}
