package io.github.tuanthhtq.swiftbillsbfree.entities;

import io.github.tuanthhtq.swiftbillsbfree.constants.Length;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author io.github.tuanthhtq
 */

@Entity
@Getter
@NoArgsConstructor
public class BankAccounts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_method_id")
	private Long id;

	@Column(nullable = false, length = Length.NAME)
	private String bankName;

	@Column(nullable = false, length = Length.TIME)
	private String bankCode;

	@Column(nullable = false, length = Length.NAME)
	private String accountNumber;

	@Column(nullable = false, length = Length.NAME)
	private String cardHolderName;

	@OneToMany(
			mappedBy = "bank",
			fetch = FetchType.LAZY
	)
	private Set<Receipts> receipts;
}
