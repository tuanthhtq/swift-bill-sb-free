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
public class Products {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Long id;

	@Column(name = "product_name", nullable = false, length = Length.NAME)
	private String name;

	@Column(length = Length.PARAGRAPH)
	private String description;

	@Column(nullable = false)
	private float price;

	@Column(nullable = false)
	private float costPrice;

	@Column(nullable = false)
	private float stock;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Stores store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "measure_unit_id")
	private MeasureUnit measureUnit;

	@Column(length = Length.ID_NUMBER)
	private String barcode;

	private float reorderLevel = 1;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brand_id")
	private Brands brand;

	@OneToMany(
			mappedBy = "product",
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL
	)
	private Set<Images> images;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "rel_product_category",
			inverseJoinColumns = @JoinColumn(name = "category_id"),
			joinColumns = @JoinColumn(name = "product_id")
	)
	private Set<Categories> categories;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supplier_id")
	private Suppliers supplier;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
	private Set<ReceiptProducts> receipts;

	@CreationTimestamp
	private Instant createdDate;

	public Products(String name, String description, float price, float costPrice, float stock, MeasureUnit measureUnit, String barcode) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.costPrice = costPrice;
		this.stock = stock;
		this.measureUnit = measureUnit;
		this.barcode = barcode;
	}

	public Products(String name, String description, float price, float costPrice, float stock, MeasureUnit measureUnit) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.costPrice = costPrice;
		this.stock = stock;
		this.measureUnit = measureUnit;
	}

	public Products(String name, String description, float price, float costPrice, float stock, MeasureUnit measureUnit, String barcode, Set<Categories> categories, Suppliers supplier, Set<Images> images, Brands brand) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.costPrice = costPrice;
		this.stock = stock;
		this.measureUnit = measureUnit;
		this.barcode = barcode;
		this.categories = categories;
		this.supplier = supplier;
		this.images = images;
		this.brand = brand;
	}
}
