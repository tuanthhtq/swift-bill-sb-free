package io.github.tuanthhtq.swiftbillsbfree.repositories;

import io.github.tuanthhtq.swiftbillsbfree.entities.Receipts;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ReceiptsRepository extends JpaRepository<Receipts, Long> {


	List<Receipts> findByCreatedDateBetween(Instant start, Instant end, Pageable pageable);

	List<Receipts> findByCreatedDateAfter(Instant start, Pageable pageable);

	List<Receipts> findByCreatedDateBefore(Instant start, Pageable pageable);

	List<Receipts> findByCustomerName(String customerName, Pageable pageable);

	List<Receipts> findByCustomerPhone(String customerPhone, Pageable pageable);

	List<Receipts> findByCreator_FullName(String cashierName, Pageable pageable);

	List<Receipts> findByCreator_Phone(String email, Pageable pageable);

	Optional<Receipts> findByOrderCode(String orderCode);
}