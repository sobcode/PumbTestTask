package com.projects.pumbtesttask.helper;

import com.projects.pumbtesttask.model.Animal;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides utility methods for working with CSV files, particularly those containing animal data.
 */
@Component
public class CSVHelper {
    public String type = "text/csv";
    public String[] headers = {"Name", "Type", "Sex", "Weight", "Cost"};
    private static final Logger log = LoggerFactory.getLogger(CSVHelper.class);

    /**
     * Checks if a MultipartFile represents a CSV file based on its content type.
     *
     * @param file The MultipartFile to be checked.
     * @return True if the file's content type is "text/csv", False otherwise.
     */
    public boolean hasCSVFormat(MultipartFile file) {
        return type.equals(file.getContentType());
    }

    /**
     * Parses a CSV InputStream containing animal data and converts it into a List of Animal objects.
     * <p>
     * This method expects the CSV file to have specific headers (defined in the `headers` field)
     * and validates the data types for certain fields (weight and cost must be non-negative integers).
     * <p>
     * If any record is missing a value, has a blank value, or contains invalid weight/cost data, it's skipped.
     *
     * @param is The InputStream containing the CSV data.
     * @return A List of Animal objects parsed from the CSV data.
     * @throws RuntimeException If there's an error parsing the CSV file.
     */
    public List<Animal> csvToAnimals(InputStream is) {
        CSVFormat.Builder builder = CSVFormat.DEFAULT.builder();
        builder.setHeader(headers);
        builder.setSkipHeaderRecord(true);
        builder.setIgnoreHeaderCase(true);
        builder.setTrim(true);

        try (BufferedReader fileReader =
                     new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, builder.build())) {
            List<Animal> animals = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            // Skip records with missing values or invalid data (weight & cost)
            for (CSVRecord csvRecord : csvRecords) {
                if (csvRecord.stream().anyMatch(String::isBlank) ||
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
        } catch (Exception e) {
            log.error("LOG: an error occurred " + e);
            throw new RuntimeException("Fail to parse CSV file: " + e);
        }
    }
}
