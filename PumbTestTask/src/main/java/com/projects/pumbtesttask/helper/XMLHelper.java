package com.projects.pumbtesttask.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.projects.pumbtesttask.model.Animal;
import com.projects.pumbtesttask.model.dto.AnimalDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class XMLHelper {
    public String type = "text/xml";

    public boolean hasXMLFormat(MultipartFile file) {
        return type.equals(file.getContentType());
    }

    public List<Animal> xmlToAnimals(InputStream is) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            List<AnimalDTO> animals = xmlMapper.readValue(is, new TypeReference<>() {});

            return animals.stream()
                    .filter(a -> a.getName() != null && a.getType() != null && a.getSex() != null
                            && a.getWeight() > 0 && a.getCost() != null && a.getCost() >= 0)
                    .map(a -> new Animal(a.getName(), a.getType(), a.getSex(), a.getWeight(), a.getCost()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse XML file: " + e);
        }
    }
}
