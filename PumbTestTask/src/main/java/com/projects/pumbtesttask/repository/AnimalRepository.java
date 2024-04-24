package com.projects.pumbtesttask.repository;

import com.projects.pumbtesttask.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
}
