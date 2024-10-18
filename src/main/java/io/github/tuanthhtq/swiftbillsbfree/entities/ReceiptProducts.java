package io.github.tuanthhtq.swiftbillsbfree.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author io.github.tuanthhtq
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "rel_receipt_product")
public class ReceiptProducts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rel_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Products product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receipt_id")
	private Receipts receipt;

	private float amount;

	private float unitPrice;

}
