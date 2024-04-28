package com.projects.pumbtesttask.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.projects.pumbtesttask.model.Animal;
import com.projects.pumbtesttask.model.dto.AnimalDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides utility methods for working with XML files, particularly those containing animal data.
 */
@Component
public class XMLHelper {
    public String type = "text/xml";
    private static final Logger log = LoggerFactory.getLogger(XMLHelper.class);

    /**
     * Checks if a MultipartFile represents an XML file based on its content type.
     *
     * @param file The MultipartFile to be checked.
     * @return True if the file's content type is "text/xml", False otherwise.
     */
    public boolean hasXMLFormat(MultipartFile file) {
        return type.equals(file.getContentType());
    }

    /**
     * Parses an XML InputStream containing animal data and converts it into a List of Animal objects.
     * <p>
     * This method expects the XML data to be unmarshalled into a List of `AnimalDTO` objects
     * (assuming a specific DTO class structure). It then performs validation on the extracted data
     * (name, type, sex, weight, cost) before converting them to `Animal` objects.
     * <p>
     * If any record is missing a value, or contains invalid weight/cost data, it's skipped.
     *
     * @param is The InputStream containing the XML data.
     * @return A List of Animal objects parsed from the XML data.
     * @throws RuntimeException If there's an error parsing the XML file.
     */
    public List<Animal> xmlToAnimals(InputStream is) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            List<AnimalDTO> animals = xmlMapper.readValue(is, new TypeReference<>() {
            });

            return animals.stream()
                    .filter(a -> a.getName() != null && a.getType() != null && a.getSex() != null
                            && a.getWeight() > 0 && a.getCost() != null && a.getCost() >= 0)
                    .map(a -> new Animal(a.getName(), a.getType(), a.getSex(), a.getWeight(), a.getCost()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("LOG: an error occurred " + e);
            throw new RuntimeException("Fail to parse XML file: " + e);
        }
    }
}
