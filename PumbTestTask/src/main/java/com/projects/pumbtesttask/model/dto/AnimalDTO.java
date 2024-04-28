package com.projects.pumbtesttask.model.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a Data Transfer Object (DTO) for animal data.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "animal")
public class AnimalDTO {
    private String name;
    private String type;
    private String sex;
    private int weight;
    private Integer cost;
    private int category; // depends on cost
}
