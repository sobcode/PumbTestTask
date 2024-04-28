package com.projects.pumbtesttask.controller;

import com.projects.pumbtesttask.model.dto.PaginatedAnimalResponseDTO;
import com.projects.pumbtesttask.model.dto.UploadResponseDTO;
import com.projects.pumbtesttask.service.AnimalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * This class is a REST controller for handling animal data.
 * It provides endpoints for uploading animal data from files with XML or CSV format
 * and searching for animals based on specific criteria.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class AnimalsController {
    private final AnimalService animalService;

    @Autowired
    public AnimalsController(AnimalService animalService) {
        this.animalService = animalService;
    }

    /**
     * Uploads animal data from a CSV or XML file.
     * This method expects a multipart form data request with a file part.
     * The uploaded file is then processed by the `AnimalService` to extract and store animal data.
     *
     * @param file The multipart file containing animal data in CSV or XML format.
     * @return A ResponseEntity object containing an UploadResponseDTO with details about the upload process.
     *         The status code of the response will indicate success or failure of the upload operation.
     */
    @PostMapping(value = "/files/uploads", consumes = {"multipart/form-data"})
    public ResponseEntity<UploadResponseDTO> uploadAnimals(@RequestPart("file") MultipartFile file) {
        return animalService.uploadAnimalsFromCsvOrXmlFile(file);
    }

    /**
     * Reads and retrieves a paginated list of animals based on search criteria.
     * This method supports searching for animals by type, category, and sex.
     * It also accepts a Pageable object to define the page size and sorting options for the results.
     *
     * @param type (Optional) Filter by animal type (e.g., dog, cat, etc.).
     * @param category (Optional) Filter by animal category (e.g., 1-4.).
     * @param sex (Optional) Filter by animal sex (e.g., male, female).
     * @param pageable The Pageable object defining page size, sorting, and other pagination options.
     * @return A ResponseEntity object containing a PaginatedAnimalResponseDTO
     *         with the retrieved animal data and pagination information.
     */
    @GetMapping("/animals/search")
    public ResponseEntity<PaginatedAnimalResponseDTO> readAnimals(@RequestParam(defaultValue = "") String type,
                                                                  @RequestParam(defaultValue = "") String category,
                                                                  @RequestParam(defaultValue = "") String sex,
                                                                  Pageable pageable) {
        return ResponseEntity.ok(animalService.readAnimals(type, category, sex, pageable));
    }
}
