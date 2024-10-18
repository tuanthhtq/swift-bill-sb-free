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

@Entity(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Categories {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long id;

	@Column(name = "name", unique = true, length = Length.NAME)
	private String name;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "rel_product_category",
			joinColumns = @JoinColumn(name = "category_id"),
			inverseJoinColumns = @JoinColumn(name = "product_id")
	)
	private Set<Products> products;

	public Categories(String name) {
		this.name = name;
	}
}
