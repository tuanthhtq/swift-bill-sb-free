package io.github.tuanthhtq.swiftbillsbfree.entities;

import io.github.tuanthhtq.swiftbillsbfree.constants.Length;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;

/**
 * @author io.github.tuanthhtq
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Receipts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "receipt_id")
	private Long id;

	@Column(unique = true)
	private String orderCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private Users creator;

	@Column(length = Length.NAME)
	private String customerName;

	@Column(length = Length.PHONE)
	private String customerPhone;

	private float amount;

	@Column(length = Length.LINK)
	private String qrCode;

	private boolean paid = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bank_id")
	private BankAccounts bank;

	@OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
	private Set<ReceiptProducts> items;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Stores store;

	@CreationTimestamp
	private Instant createdDate;

	@UpdateTimestamp
	private Instant paymentDate;

}
