package com.projects.pumbtesttask.service;

import com.projects.pumbtesttask.model.Animal;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CSVService {
    void save(MultipartFile multipartFile);
}
