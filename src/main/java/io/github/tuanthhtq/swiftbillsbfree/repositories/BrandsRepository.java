package io.github.tuanthhtq.swiftbillsbfree.repositories;

import io.github.tuanthhtq.swiftbillsbfree.entities.Brands;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandsRepository extends JpaRepository<Brands, Long> {

	Optional<Brands> findByName(String brand);
}