package io.github.tuanthhtq.swiftbillsbfree.dtos.product;

/**
 * @author io.github.tuanthhtq
 */

public record ProductSearchCriteria(
		String barcode,
		Long productId,
		Long storeId
) {
}
