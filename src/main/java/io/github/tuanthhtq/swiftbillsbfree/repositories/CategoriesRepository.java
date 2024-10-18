package io.github.tuanthhtq.swiftbillsbfree.repositories;

import io.github.tuanthhtq.swiftbillsbfree.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {

	Optional<Categories> findByName(String name);
}