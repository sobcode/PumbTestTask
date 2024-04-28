package com.projects.pumbtesttask.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * This interface defines methods for saving animals data from files.
*/
public interface FileService {
    void save(MultipartFile multipartFile);
}
