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

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MeasureUnit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "measure_unit")
	private Long id;

	@Column(name = "measure_unit_name", unique = true, length = Length.NAME)
	private String name;

	@OneToMany(
			mappedBy = "measureUnit",
			fetch = FetchType.LAZY
	)
	private Set<Products> products;

	public MeasureUnit(String name) {
		this.name = name;
	}
}
