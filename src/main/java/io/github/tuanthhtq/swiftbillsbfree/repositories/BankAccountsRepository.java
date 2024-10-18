package io.github.tuanthhtq.swiftbillsbfree.repositories;

import io.github.tuanthhtq.swiftbillsbfree.entities.BankAccounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountsRepository extends JpaRepository<BankAccounts, Long> {
}