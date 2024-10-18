package io.github.tuanthhtq.swiftbillsbfree.entities;

import io.github.tuanthhtq.swiftbillsbfree.constants.Length;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Set;

/**
 * @author io.github.tuanthhtq
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Suppliers {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "supplier_id")
	private Long id;

	@Column(name = "name", nullable = false, length = Length.NAME)
	private String name;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false, length = Length.PHONE)
	private String phone;

	@OneToMany(
			fetch = FetchType.LAZY,
			mappedBy = "supplier"
	)
	private Set<Products> products;

	@CreationTimestamp
	private Instant createdDate;

	public Suppliers(String name, String address, String phone) {
		this.name = name;
		this.address = address;
		this.phone = phone;
	}

	public Suppliers(String phone, String name) {
		this.phone = phone;
		this.name = name;
	}
}
