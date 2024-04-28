package com.projects.pumbtesttask.model;

import com.projects.pumbtesttask.model.dto.AnimalDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents an animal entity.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "animals")
public class Animal {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // primary key

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "sex")
    private String sex;

    @Column(name = "weight")
    private int weight;

    @Column(name = "cost")
    private int cost;

    @Column(name = "category")
    private int category;

    public Animal(String name, String type, String sex, int weight, int cost) {
        this.name = name;
        this.type = type;
        this.sex = sex;
        this.weight = weight;
        this.cost = cost;

        setCategory();
    }

    public static Animal getAnimalFromAnimalDTO(AnimalDTO animalDTO) {
        return new Animal(animalDTO.getName(), animalDTO.getType(), animalDTO.getSex(),
                animalDTO.getWeight(), animalDTO.getCost());
    }

    public void setCost(int cost) {
        this.cost = cost;
        setCategory();
    }

    /**
     * Sets the animal's category based on its cost.
     * <p>
     * This private helper method is called by the class constructor and cost setter
     * (whenever the cost is updated).
     * It assigns a category value (1-4) based on cost ranges.
     */
    private void setCategory() {
        if(cost >= 0 && cost <= 20) {
            category = 1;
        } else if(cost < 40) {
            category = 2;
        } else if(cost <= 60) {
            category = 3;
        } else {
            category = 4;
        }
    }
}
