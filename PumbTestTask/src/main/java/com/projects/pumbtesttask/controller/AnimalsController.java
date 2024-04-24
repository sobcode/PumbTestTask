package com.projects.pumbtesttask.controller;

import com.projects.pumbtesttask.helper.CSVHelper;
import com.projects.pumbtesttask.model.dto.AnimalDTO;
import com.projects.pumbtesttask.model.dto.UploadResponseDTO;
import com.projects.pumbtesttask.service.AnimalService;
import com.projects.pumbtesttask.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AnimalsController {
    private final AnimalService animalService;
    private final CSVService csvService;

    @Autowired
    public AnimalsController(AnimalService animalService, CSVService csvService) {
        this.animalService = animalService;
        this.csvService = csvService;
    }

    @GetMapping("/")
    public String testController() {
        return "Hello";
    }

    @PostMapping(value = "/files/uploads", consumes = {"multipart/form-data"})
    public ResponseEntity<UploadResponseDTO> uploadAnimals(@RequestPart("file") MultipartFile file) {
        String message;

        if(CSVHelper.hasCSVFormat(file)) {
            try {
                csvService.save(file);

                message = "The csv file uploaded successfully";
                return ResponseEntity.ok(new UploadResponseDTO(message));
            } catch (Exception e) {
                message = "Could not upload the file " + file.getOriginalFilename() + "! " + e;
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                        .body(new UploadResponseDTO(message));
            }
        }

        message = "Please upload a csv file or a xml file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UploadResponseDTO(message));
    }

    @PostMapping
    public boolean createAnimal(@RequestBody AnimalDTO animalDTO) {

        animalService.createAnimal(animalDTO);
        return true;
    }

}
