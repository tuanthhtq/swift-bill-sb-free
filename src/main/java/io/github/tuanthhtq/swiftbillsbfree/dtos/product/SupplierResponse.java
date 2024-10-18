package io.github.tuanthhtq.swiftbillsbfree.dtos.product;

import java.time.Instant;

/**
 * @author io.github.tuanthhtq
 */

public record SupplierResponse(
		Long id,
		String name,
		String address,
		String phone,
		Instant createdDate
) {
}
