package io.github.tuanthhtq.swiftbillsbfree.dtos.payment;

import java.time.Instant;

/**
 * @author io.github.tuanthhtq
 */

public record ReceiptInfo(
		String customerName,
		String customerPhone,
		Instant creationDate,
		String cashierName,
		boolean paid,
		float amount
) {
}
