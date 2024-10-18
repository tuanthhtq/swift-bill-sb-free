package io.github.tuanthhtq.swiftbillsbfree.dtos.bill;

import java.time.Instant;

/**
 * @author io.github.tuanthhtq
 */

public record BillInfoResponse(
		String customerName,
		Instant creationDate,
		String sellerName,
		boolean paid,
		float amount
) {
}
