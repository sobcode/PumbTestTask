package com.projects.pumbtesttask.controller;

import com.projects.pumbtesttask.helper.CSVHelper;
import com.projects.pumbtesttask.helper.XMLHelper;
import com.projects.pumbtesttask.model.dto.UploadResponseDTO;
import com.projects.pumbtesttask.service.impl.CSVServiceImpl;
import com.projects.pumbtesttask.service.impl.XMLServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AnimalsController {
    private final CSVServiceImpl csvService;
    private final XMLServiceImpl xmlService;

    @Autowired
    public AnimalsController(CSVServiceImpl csvService, XMLServiceImpl xmlService) {
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
}
