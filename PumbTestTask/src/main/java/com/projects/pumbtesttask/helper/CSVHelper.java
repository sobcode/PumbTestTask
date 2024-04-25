package com.projects.pumbtesttask.helper;

import com.projects.pumbtesttask.model.Animal;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {
    public static String type = "text/csv";
    public static String[] headers = {"Name", "Type", "Sex", "Weight", "Cost"};

    public static boolean hasCSVFormat(MultipartFile file) {
        return type.equals(file.getContentType());
    }

    public static List<Animal> csvToAnimals(InputStream is) {
        CSVFormat.Builder builder = CSVFormat.DEFAULT.builder();
        builder.setHeader(headers);
        builder.setSkipHeaderRecord(true);
        builder.setIgnoreHeaderCase(true);
        builder.setTrim(true);

        try (BufferedReader fileReader =
                     new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, builder.build()))
        {
            List<Animal> animals = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for(CSVRecord csvRecord : csvRecords) {
                if(csvRecord.stream().anyMatch(String::isBlank) ||
                        Integer.parseInt(csvRecord.get("Weight")) < 0 ||
                        Integer.parseInt(csvRecord.get("Cost")) < 0) {
                    continue;
                }

                Animal animal = new Animal(csvRecord.get("Name"),
                        csvRecord.get("Type"),
                        csvRecord.get("Sex"),
                        Integer.parseInt(csvRecord.get("Weight")),
                        Integer.parseInt(csvRecord.get("Cost")));

                animals.add(animal);
            }

            return animals;
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse CSV file: " + e);
        }
    }
}
