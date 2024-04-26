package com.projects.pumbtesttask.controller;

import com.projects.pumbtesttask.helper.CSVHelper;
import com.projects.pumbtesttask.helper.XMLHelper;
import com.projects.pumbtesttask.model.dto.PaginatedAnimalResponseDTO;
import com.projects.pumbtesttask.model.dto.UploadResponseDTO;
import com.projects.pumbtesttask.service.AnimalService;
import com.projects.pumbtesttask.service.impl.CSVServiceImpl;
import com.projects.pumbtesttask.service.impl.XMLServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
public class AnimalsController {
    private final AnimalService animalService;
    private final CSVServiceImpl csvService;
    private final XMLServiceImpl xmlService;

    @Autowired
    public AnimalsController(AnimalService animalService, CSVServiceImpl csvService, XMLServiceImpl xmlService) {
        this.animalService = animalService;
        this.csvService = csvService;
        this.xmlService = xmlService;
    }

    @GetMapping("/")
    public String testController() {
        return "Hello";
    }

    @PostMapping(value = "/files/uploads", consumes = {"multipart/form-data"})
    public ResponseEntity<UploadResponseDTO> uploadAnimals(@RequestPart("file") MultipartFile file) {
        String message;
        try {
            if (CSVHelper.hasCSVFormat(file)) {
                csvService.save(file);
                message = "The csv file uploaded successfully";
                return ResponseEntity.ok(new UploadResponseDTO(message));
            }
            else if (XMLHelper.hasXMLFormat(file)) {
                xmlService.save(file);
                message = "The xml file uploaded successfully";
                return ResponseEntity.ok(new UploadResponseDTO(message));
            }
        } catch (Exception e) {
            message = "Could not upload the file " + file.getOriginalFilename() + "! " + e;
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new UploadResponseDTO(message));
        }

        message = "Please upload a csv or xml file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UploadResponseDTO(message));
    }

    @GetMapping("/animals/search")
    public ResponseEntity<PaginatedAnimalResponseDTO> readAnimals(@RequestParam(defaultValue = "") String type,
                                                                  @RequestParam(defaultValue = "") String category,
                                                                  @RequestParam(defaultValue = "") String sex,
                                                                  Pageable pageable) {

        if(sortIsValid(pageable.getSort())) {
            throw new IllegalArgumentException("Invalid sort field!");
        }

        return ResponseEntity.ok(animalService.readAnimals(type, category, sex, pageable));
    }

    private boolean sortIsValid(Sort sort) {
        List<String> validSortFields = Arrays.asList("name", "type", "sex", "weight", "cost", "category");

        if(sort != null) {
            for(Sort.Order order : sort) {
                String fieldName = order.getProperty();

                if(validSortFields.contains(fieldName)) {
                    return false;
                }
            }
        }
        return true;
    }
}
