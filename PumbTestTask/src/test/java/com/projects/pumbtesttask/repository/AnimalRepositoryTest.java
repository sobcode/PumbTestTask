package com.projects.pumbtesttask.repository;

import com.projects.pumbtesttask.model.Animal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@DataJpaTest
public class AnimalRepositoryTest {
    @Autowired
    private AnimalRepository animalRepository;
    private final String name = "Simon";
    private final String type = "cat";
    private final String sex = "male";

    @Test
    public void testSaveAnimal() {
        Animal animal = animalRepository.save(
                new Animal(name, type, sex, 10, 100));
        assertTrue(animal.getId() != 0);
    }

    @Test
    public void testFindAllAnimals() {
        for (int i = 0; i < 3; i++) {
            animalRepository.save(
                    new Animal("Simon" + i, "cat", "male", 10, 100 * i));
        }
        assertEquals(3, animalRepository.findAll().size());
    }

    @Test
    public void testFindAllAnimalsByTypeContainsAndSexLike() {
        animalRepository.save(new Animal(name, type, sex, 10, 100));
        animalRepository.save(new Animal("Linda", "dog", "female", 10, 100));

        List<Animal> animals = animalRepository.findAllByTypeContainsAndSexLike(type, sex, Pageable.unpaged())
                .getContent();

        assertEquals(1, animals.size());
        assertEquals(name, animals.get(0).getName());
    }

    @Test
    public void testFindAllAnimalsByTypeContainsAndCategoryEqualsAndSexLike() {
        Animal targetAnimal = new Animal(name, type, sex, 10, 100);
        animalRepository.save(targetAnimal);
        animalRepository.save(new Animal("Linda", "dog", "female", 10, 10));

        List<Animal> animals = animalRepository
                .findAllByTypeContainsAndCategoryEqualsAndSexLike(type, targetAnimal.getCategory(), sex, Pageable.unpaged())
                .getContent();

        assertEquals(1, animals.size());
        assertEquals(targetAnimal.getCategory(), animals.get(0).getCategory());
        assertEquals(name, animals.get(0).getName());
    }
}
