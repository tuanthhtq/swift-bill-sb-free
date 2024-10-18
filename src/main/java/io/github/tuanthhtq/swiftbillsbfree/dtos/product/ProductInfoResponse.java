package io.github.tuanthhtq.swiftbillsbfree.dtos.product;

import java.util.Set;

/**
 * @author io.github.tuanthhtq
 */

public record ProductInfoResponse(
		Long id,
		String name,
		String brandName,
		Set<String> images,
		float price
) {
}
