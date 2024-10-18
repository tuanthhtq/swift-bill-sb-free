package io.github.tuanthhtq.swiftbillsbfree.entities;

import io.github.tuanthhtq.swiftbillsbfree.constants.Length;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * @author io.github.tuanthhtq
 */

@Entity(name = "brands")
@Getter
@Setter
@NoArgsConstructor
public class Brands {

	@Id
	@Column(name = "brand_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, length = Length.NAME)
	private String name;

	@OneToMany(
			mappedBy = "brand",
			fetch = FetchType.LAZY
	)
	private Set<Products> products;

	public Brands(String name) {
		this.name = name;
	}
}
