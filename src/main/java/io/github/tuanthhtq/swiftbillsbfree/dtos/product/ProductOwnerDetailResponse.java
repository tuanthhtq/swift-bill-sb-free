package io.github.tuanthhtq.swiftbillsbfree.dtos.product;

import java.time.Instant;
import java.util.Set;

/**
 * @author io.github.tuanthhtq
 */

public record ProductOwnerDetailResponse(
		Long id,
		String name,
		String description,
		float price,
		float costPrice,
		float stock,
		String measureUnit,
		String barcode,
		float reorderLevel,
		Boolean available,
		BrandResponse brand,
		Set<String> images,
		CategoryResponse category,
		SupplierResponse supplier,
		Instant createdDate,
		Instant modifiedDate
) {
}