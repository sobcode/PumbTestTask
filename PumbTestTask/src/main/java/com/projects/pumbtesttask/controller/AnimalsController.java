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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class AnimalsController {
    private final AnimalService animalService;

    @Autowired
    public AnimalsController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PostMapping(value = "/files/uploads", consumes = {"multipart/form-data"})
    public ResponseEntity<UploadResponseDTO> uploadAnimals(@RequestPart("file") MultipartFile file) {
        return animalService.uploadAnimalsFromCsvOrXmlFile(file);
    }

    @GetMapping("/animals/search")
    public ResponseEntity<PaginatedAnimalResponseDTO> readAnimals(@RequestParam(defaultValue = "") String type,
                                                                  @RequestParam(defaultValue = "") String category,
                                                                  @RequestParam(defaultValue = "") String sex,
                                                                  Pageable pageable) {

        return ResponseEntity.ok(animalService.readAnimals(type, category, sex, pageable));
    }
}
