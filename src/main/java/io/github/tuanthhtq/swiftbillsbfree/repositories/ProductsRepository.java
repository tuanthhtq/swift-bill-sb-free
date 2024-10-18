package io.github.tuanthhtq.swiftbillsbfree.repositories;

import io.github.tuanthhtq.swiftbillsbfree.entities.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Products, Long> {
	Optional<Products> findByBarcode(String barcode);

}