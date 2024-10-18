package io.github.tuanthhtq.swiftbillsbfree.dtos.payment;

/**
 * @author io.github.tuanthhtq
 */

public record PaymentItem(
		//common for request and response
		String barcode,
		float amount,

		//for response
		String productName,
		String measureUnit,
		float unitPrice
) {
}
