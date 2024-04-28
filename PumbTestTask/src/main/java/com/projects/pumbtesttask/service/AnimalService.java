package com.projects.pumbtesttask.service;

import com.projects.pumbtesttask.model.Animal;
import com.projects.pumbtesttask.model.dto.AnimalDTO;
import com.projects.pumbtesttask.model.dto.PaginatedAnimalResponseDTO;
import com.projects.pumbtesttask.model.dto.UploadResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AnimalService {
    PaginatedAnimalResponseDTO readAnimals(String type, String category, String sex, Pageable pageable);

    ResponseEntity<UploadResponseDTO> uploadAnimalsFromCsvOrXmlFile(MultipartFile file);
}
