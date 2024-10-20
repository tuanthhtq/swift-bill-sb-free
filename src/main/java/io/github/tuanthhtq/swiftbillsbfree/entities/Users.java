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

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	// login information
	@Column(nullable = false, unique = true, length = Length.PHONE)
	private String phone;

	@Column(nullable = false, length = Length.PASSWORD)
	private String password;

	//details
	@Column(unique = true, nullable = false, length = Length.ID_NUMBER)
	private String idNumber;

	@Column(length = Length.NAME)
	private String fullName;

	@Column(length = Length.PHONE)
	private String phone2;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "rel_store_employee",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "store_id")
	)
	private Set<Stores> stores;

	@OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
	private Set<Stores> ownedStores;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "rel_user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Roles> roles;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private Addresses address;

	@OneToMany(
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			mappedBy = "creator"
	)
	private Set<Receipts> receipts;

	//metadata
	@CreationTimestamp
	private Instant createdDate;

	public Users(String phone, String password, String idNumber, String fullName, Set<Roles> roles) {
		this.phone = phone;
		this.password = password;
		this.idNumber = idNumber;
		this.fullName = fullName;
		this.roles = roles;
	}
}