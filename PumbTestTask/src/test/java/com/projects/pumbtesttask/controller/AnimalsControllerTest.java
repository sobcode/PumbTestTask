package com.projects.pumbtesttask.controller;

import com.projects.pumbtesttask.model.dto.PaginatedAnimalResponseDTO;
import com.projects.pumbtesttask.model.dto.UploadResponseDTO;
import com.projects.pumbtesttask.service.AnimalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AnimalsController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class AnimalsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AnimalService animalService;

    @Test
    public void testUploadAnimals() throws Exception {
        MockMultipartFile csvFile = new MockMultipartFile("file",
                "animals.csv", "text/csv", "CSV content".getBytes());

        when(animalService.uploadAnimalsFromCsvOrXmlFile(any(MultipartFile.class)))
                .thenReturn(ResponseEntity.ok(new UploadResponseDTO("Test")));


        ResultActions response = mockMvc.perform(multipart("/api/files/uploads").file(csvFile));
        response.andExpect(status().isOk());
    }

    @Test
    public void testReadAnimals() throws Exception {
        when(animalService.readAnimals(anyString(), anyString(), anyString(), any(Pageable.class)))
                .thenReturn(new PaginatedAnimalResponseDTO(List.of(), 0, 0));

        mockMvc.perform(get("/api/animals/search")
                        .param("type", "")
                        .param("category", "")
                        .param("sex", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
