package com.projects.pumbtesttask.repository;

import com.projects.pumbtesttask.model.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Page<Animal> findAllByTypeContainsAndSexLike(String type, String sex, Pageable pageable);

    Page<Animal> findAllByTypeContainsAndCategoryEqualsAndSexLike(String type, int category, String sex, Pageable pageable);
}
