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
public class Stores {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_id")
	private Long id;

	@Column(nullable = false, length = Length.NAME, name = "name")
	private String storeName;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "rel_store_employee",
			joinColumns = @JoinColumn(name = "store_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private Set<Users> employees;

	@OneToMany(
			mappedBy = "store",
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY
	)
	Set<Products> products;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private Addresses address;

	@OneToMany(
			fetch = FetchType.LAZY,
			mappedBy = "store",
			cascade = CascadeType.ALL
	)
	Set<Receipts> receipts;

	@Column(length = Length.TIME)
	private String openTime = "08:00 AM";

	@Column(length = Length.TIME)
	private String closeTime = "08:00 PM";

	@CreationTimestamp
	private Instant createdDate;

	//constructor with all information
	public Stores(String storeName, Addresses address, String openTime, String closeTime, Set<Users> employees) {
		this.storeName = storeName;
		this.address = address;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.employees = employees;
	}

	//without specifying owner
	public Stores(String storeName, Addresses address, String openTime, String closeTime) {
		this.storeName = storeName;
		this.address = address;
		this.openTime = openTime;
		this.closeTime = closeTime;
	}

}
