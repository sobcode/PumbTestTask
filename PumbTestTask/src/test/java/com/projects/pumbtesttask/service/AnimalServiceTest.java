package com.projects.pumbtesttask.service;

import com.projects.pumbtesttask.helper.CSVHelper;
import com.projects.pumbtesttask.helper.XMLHelper;
import com.projects.pumbtesttask.model.Animal;
import com.projects.pumbtesttask.model.dto.PaginatedAnimalResponseDTO;
import com.projects.pumbtesttask.model.dto.UploadResponseDTO;
import com.projects.pumbtesttask.repository.AnimalRepository;
import com.projects.pumbtesttask.service.impl.AnimalServiceImpl;
import com.projects.pumbtesttask.service.impl.XMLServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {
    @InjectMocks
    private AnimalServiceImpl animalService;
    @Mock
    private AnimalRepository animalRepository;
    @Mock
    private XMLServiceImpl xmlService;
    @Mock
    private XMLHelper xmlHelper;
    @Mock
    private CSVHelper csvHelper;

    @Test
    public void testReadAnimalsTestWithoutFilteringByCategory() {
        List<Animal> animals = List.of(new Animal(), new Animal());

        when(animalRepository.findAllByTypeContainsAndSexLike(anyString(), anyString(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(animals));

        PaginatedAnimalResponseDTO responseDTO = animalService.
                readAnimals("cat", "", "male", PageRequest.of(1, 2));

        assertEquals(animals.size(), responseDTO.getNumberOfItems());
    }

    @Test
    public void testReadAnimalsTestWithFilteringByCategory() {
        List<Animal> animals = List.of(new Animal(), new Animal());

        when(animalRepository.findAllByTypeContainsAndCategoryEqualsAndSexLike(anyString(), anyInt(),
                anyString(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(animals));

        PaginatedAnimalResponseDTO responseDTO = animalService.
                readAnimals("cat", "1", "male", PageRequest.of(1, 2));

        assertEquals(animals.size(), responseDTO.getNumberOfItems());
    }

    @Test
    public void testReadAnimalsTestWithInvalidSortField() {
        assertThrows(IllegalArgumentException.class,
                () -> animalService.readAnimals("bird", "", "male",
                        Pageable.unpaged(Sort.by("age"))));
    }

    @Test
    public void testSuccessfulUploadCsvFile() {
        MockMultipartFile csvFile = new MockMultipartFile("file",
                "animals.csv", "text/csv", "CSV content".getBytes());

        when(csvHelper.hasCSVFormat(any(MultipartFile.class))).thenReturn(true);

        ResponseEntity<UploadResponseDTO> response = animalService.uploadAnimalsFromCsvOrXmlFile(csvFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The csv file uploaded successfully", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void testSuccessfulUploadXmlFile() {
        MockMultipartFile xmlFile = new MockMultipartFile("file",
                "animals.xml", "text/xml", "XML content".getBytes());

        when(csvHelper.hasCSVFormat(any(MultipartFile.class))).thenReturn(false);
        when(xmlHelper.hasXMLFormat(any(MultipartFile.class))).thenReturn(true);

        ResponseEntity<UploadResponseDTO> response = animalService.uploadAnimalsFromCsvOrXmlFile(xmlFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The xml file uploaded successfully", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void testUploadFileWithArithmeticException() {
        MockMultipartFile xmlFile = new MockMultipartFile("file",
                "animals.xml", "text/xml", "XML content".getBytes());
        when(xmlHelper.hasXMLFormat(any(MultipartFile.class))).thenReturn(true);
        doThrow(ArithmeticException.class).when(xmlService).save(any(MultipartFile.class));

        assertThrows(RuntimeException.class, () -> animalService.uploadAnimalsFromCsvOrXmlFile(xmlFile));
    }

    @Test
    public void uploadFileWithIllegalFileFormat() {
        MockMultipartFile file = new MockMultipartFile("file",
                "animals.txt", "text/txt", "Some text".getBytes());

        when(csvHelper.hasCSVFormat(any(MultipartFile.class))).thenReturn(false);
        when(xmlHelper.hasXMLFormat(any(MultipartFile.class))).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> animalService.uploadAnimalsFromCsvOrXmlFile(file));
    }


}
