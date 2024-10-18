package io.github.tuanthhtq.swiftbillsbfree.dtos.product;

import java.util.Set;

/**
 * @author io.github.tuanthhtq
 */

public record ProductViewerDetailResponse(
		String name,
		String description,
		float price,
		float stock,
		String measureUnit,
		String barcode,
		Boolean available,
		String brandName,
		Set<String> images,
		String category,
		String supplierName
) {
}
