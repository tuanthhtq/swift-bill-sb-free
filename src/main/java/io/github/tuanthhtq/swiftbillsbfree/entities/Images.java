package io.github.tuanthhtq.swiftbillsbfree.entities;

import io.github.tuanthhtq.swiftbillsbfree.constants.Length;
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
public class Images {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_image_id")
	private Long id;

	@Column(name = "image_url", nullable = false, length = Length.LINK)
	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Products product;

	public Images(String url, Products product) {
		this.url = url;
		this.product = product;
	}

	public Images(String url) {
		this.url = url;
	}
}
