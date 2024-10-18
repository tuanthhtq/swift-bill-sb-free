package io.github.tuanthhtq.swiftbillsbfree.dtos.bill;

import java.time.Instant;

/**
 * @author io.github.tuanthhtq
 */

public record BillDetailResponse(
		int orderCode,
		String cashierName,
		float amount,
		String customerName,
		String customerPhone,
		String paymentMethod,
		boolean status,
		Instant createdDate,

		String storeName,
		String storeAddress,
		String ownerPhone,
		Instant open,
		Instant close
) {
}
