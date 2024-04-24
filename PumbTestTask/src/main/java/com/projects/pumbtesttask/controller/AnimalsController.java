package com.projects.pumbtesttask.controller;

import com.projects.pumbtesttask.model.dto.AnimalDTO;
import com.projects.pumbtesttask.model.dto.UploadAnimalsDTO;
import com.projects.pumbtesttask.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnimalsController {
    private final AnimalService animalService;

    @Autowired
    public AnimalsController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping("/")
    public String testController() {
        return "Hello";
    }

    @PostMapping("/files/uploads")
    public ResponseEntity<UploadAnimalsDTO> uploadAnimals() {


        return ResponseEntity.ok(new UploadAnimalsDTO(3)); // todo: change to actual
    }

    @PostMapping
    public boolean createAnimal(@RequestBody AnimalDTO animalDTO) {

        animalService.createAnimal(animalDTO);
        return true;
    }

}
