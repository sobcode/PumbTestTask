package com.projects.pumbtesttask.helper;

import com.projects.pumbtesttask.model.Animal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CSVHelperTest {
    @InjectMocks
    private CSVHelper csvHelper;

    @Test
    public void testHasCsvFormat() {
        MockMultipartFile csvFile = new MockMultipartFile("file",
                "animals.csv", "text/csv", "CSV content".getBytes());

        assertTrue(csvHelper.hasCSVFormat(csvFile));
    }

    @Test
    public void testValidCsvToAnimals() {
        String name = "Simon";
        int weight = 13;
        String validCsv = String.format("""
                Name,Type,Sex,Weight,Cost
                %s,cat,male,%d,100
                """, name, weight);
        InputStream inputStream = new ByteArrayInputStream(validCsv.getBytes(StandardCharsets.UTF_8));

        List<Animal> animals = csvHelper.csvToAnimals(inputStream);

        Animal animal = animals.get(0);

        assertEquals(1, animals.size());
        assertEquals(name, animal.getName());
        assertEquals(weight, animal.getWeight());
    }

    @Test
    public void testValidCsvWithBlankFieldsToAnimals() {
        String[] animalNames = new String[] {"Simon", "Bart"};
        String validCsv = String.format("""
                Name,Type,Sex,Weight,Cost
                %s,cat,male,10,100
                Linda,dog,,30,150
                ,,,,
                %s,dog,male,25,0
                """, animalNames[0], animalNames[1]);
        InputStream inputStream = new ByteArrayInputStream(validCsv.getBytes(StandardCharsets.UTF_8));

        List<Animal> animals = csvHelper.csvToAnimals(inputStream);
        Set<String> names = animals.stream()
                        .map(Animal::getName)
                        .collect(Collectors.toSet());

        assertEquals(2, animals.size());
        assertTrue(names.contains(animalNames[0]));
        assertTrue(names.contains(animalNames[1]));
    }

    @Test
    public void testValidCsvWithNegativeWeightAndCostFieldsToAnimals() {
        String name = "Simon";
        String validCsv = String.format("""
                Name,Type,Sex,Weight,Cost
                Kor,dog,female,-10,100
                %s,cat,male,11,110
                Linda,dog,female,30,-150
                """, name);
        InputStream inputStream = new ByteArrayInputStream(validCsv.getBytes(StandardCharsets.UTF_8));

        List<Animal> animals = csvHelper.csvToAnimals(inputStream);
        Animal animal = animals.get(0);

        assertEquals(1, animals.size());
        assertEquals(name, animal.getName());
    }
}
