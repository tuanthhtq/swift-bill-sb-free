package io.github.tuanthhtq.swiftbillsbfree.dtos.payment;

import java.time.Instant;

/**
 * @author io.github.tuanthhtq
 */

public record ReceiptDetail(
		String orderCode,
		String cashierName,
		float amount,
		String customerName,
		String customerPhone,
		boolean status,

		Instant createdDate,
		Instant paymentDate,

		String storeName,
		String storeAddress,
		String ownerPhone
) {
}
