package io.github.tuanthhtq.swiftbillsbfree.entities;

import io.github.tuanthhtq.swiftbillsbfree.constants.Length;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * @author io.github.tuanthhtq
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Addresses {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
	private Long id;

	@Column(nullable = false, length = Length.NAME)
	private String cityOrProvince;

	@Column(nullable = false, length = Length.NAME)
	private String district;

	@Column(nullable = false, length = Length.NAME)
	private String ward;

	@Column(nullable = false, length = Length.NAME)
	private String detailedAddress;

	@OneToOne(mappedBy = "address")
	private Users user;

	@OneToOne(mappedBy = "address")
	private Stores store;

	@CreationTimestamp
	private Instant createdDate;

	public Addresses(String cityOrProvince, String district, String ward, String detailedAddress) {
		this.cityOrProvince = cityOrProvince;
		this.district = district;
		this.ward = ward;
		this.detailedAddress = detailedAddress;
	}

	public String toString() {
		return String.format(
				"%s, %s ward, %s district, %s",
				detailedAddress, ward, district, cityOrProvince
		);
	}

}
