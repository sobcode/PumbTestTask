package com.projects.pumbtesttask.repository;

import com.projects.pumbtesttask.model.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface extends JpaRepository and provides additional custom methods for searching animal entities.
 */
@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    /**
     * This method searches for animals where the `type` attribute contains the provided string
     * and the `sex` attribute partially matches the provided string.
     *
     * @param type (Optional) The type of animal to search for.
     * @param sex (Optional) The sex of the animal to search for.
     * @param pageable The Pageable object specifying sorting and pagination options for the results.
     * @return A Page object containing the search results and pagination information.
     */
    Page<Animal> findAllByTypeContainsAndSexLike(String type, String sex, Pageable pageable);

    /**
     * This method searches for animals where the `type` attribute contains the provided string,
     * the `category` attribute matches the provided value, and the `sex` attribute partially matches
     * the provided string.
     *
     * @param type (Optional) The type of animal to search for.
     * @param category The category of the animal to search for.
     * @param sex (Optional) The sex of the animal to search for.
     * @param pageable The Pageable object specifying sorting and pagination options for the results.
     * @return A Page object containing the search results and pagination information.
     */
    Page<Animal> findAllByTypeContainsAndCategoryEqualsAndSexLike(String type, int category, String sex, Pageable pageable);
}
